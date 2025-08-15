package com.example.smartexpensetracker.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReportScreen(vm: ExpenseViewModel) {
    val all by vm.allExpenses.collectAsState()
    val last7 = (0..6).map { LocalDate.now().minusDays(it.toLong()) }.reversed()
    val dailyTotals = last7.associateWith { d -> all.filter { it.localDate == d }.sumOf { it.amount } }
    val byCategory = all.filter { it.localDate >= last7.first() }.groupBy { it.category }.mapValues { it.value.sumOf { e -> e.amount } }
    
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Last 7 Days — Daily Totals", style = MaterialTheme.typography.titleMedium)
        BarChart(dailyTotals.values.map { it.toFloat() })
        
        Text("Category-wise Totals (7d)", style = MaterialTheme.typography.titleMedium)
        Text(byCategory.entries.joinToString { "${it.key}: ₹${it.value}" })
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val context = LocalContext.current
            Button(onClick = { exportCsvAndShare(context, dailyTotals) }) { Text("Export CSV & Share") }
            Button(onClick = { vm.sync() }) { Text("Sync (Mock)") }
        }
    }
}

@Composable
private fun BarChart(values: List<Float>, barWidth: Float = 40f, barSpace: Float = 24f) {
    val max = (values.maxOrNull() ?: 1f)
    val norm = values.map { if (max == 0f) 0f else it / max }
    Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
        val h = size.height
        var x = 0f
        norm.forEach { v ->
            val barHeight = v * (h - 8f)
            drawRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(x, h - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
            x += (barWidth + barSpace)
        }
    }
}


private fun exportCsvAndShare(context: Context, dailyTotals: Map<LocalDate, Long>) {
    val file = File(context.cacheDir, "expense_report.csv")
    val fmt = DateTimeFormatter.ISO_DATE
    file.writeText("date,total\n" + dailyTotals.entries.joinToString("\n") { "${it.key.format(fmt)},${it.value}" })
    
    val uri: Uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file
    )
    
    val share = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(share, "Share CSV"))
}