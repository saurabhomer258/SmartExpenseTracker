package com.example.smartexpensetracker.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartexpensetracker.domain.Category
import com.example.smartexpensetracker.domain.Expense
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ListScreen(vm: ExpenseViewModel) {
    var filterDate by remember { mutableStateOf(LocalDate.now()) }
    var groupByCategory by remember { mutableStateOf(false) }
    
    val list by vm.allExpenses.collectAsState()
    val dayList = list.filter { it.localDate == filterDate }
    
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row( modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { filterDate = filterDate.minusDays(1) }) { Text("◀ Prev") }
            Text(
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                text = filterDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")), style = MaterialTheme.typography.titleMedium)
            Button(onClick = { filterDate = filterDate.plusDays(1) }) { Text("Next ▶") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip( selected = groupByCategory, onClick = { groupByCategory = !groupByCategory }, label = { Text("Group by Category") })
            Text(modifier = Modifier.weight(1f), text = "Count: ${dayList.size}", textAlign = TextAlign.Center)
            Text("Total: ₹${dayList.sumOf { it.amount }}")
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dayList, key = { it.id }) { e -> ExpenseRow(e) { vm.removeExpense(e.id) } }
        }
        
        AnimatedVisibility(visible = dayList.isNotEmpty(), enter = expandVertically(), exit = shrinkVertically()) {
            if (!groupByCategory) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(dayList, key = { it.id }) { e -> ExpenseRow(e) { vm.removeExpense(e.id) } }
                }
            } else {
                val groups = dayList.groupBy { it.category }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    groups.forEach { (cat, items) ->
                        item { Text(cat.name, style = MaterialTheme.typography.titleMedium) }
                        items(items, key = { it.id }) { e -> ExpenseRow(e) { vm.removeExpense(e.id) } }
                    }
                }
            }
        }
        if (dayList.isEmpty()) Text("No expenses for this day.")
    }
}

@Composable
private fun ExpenseRow(e: Expense, onDelete: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text(e.title, style = MaterialTheme.typography.titleMedium); Text("₹${e.amount} • ${e.category}") }
            TextButton(onClick = onDelete) { Text("Delete") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListScreen() {
    // Create some mock expenses
    val sampleExpenses = listOf(
        Expense(
            title = "Lunch",
            amount = 150,
            category = Category.Food
        ),
        Expense(
            title = "Taxi",
            amount = 200,
            category = Category.Travel
        )
    )
    
//    // Mock ViewModel
//    val mockVm = object : ExpenseViewModel() {
//
//    }
  
    val mockVm by lazy { ExpenseViewModel() }
    mockVm.addExpense("Lunch",100,Category.Food , null,null)
    mockVm.addExpense("Lunch",1200,Category.Food , null,null)
    MaterialTheme {
       ListScreen(vm = mockVm)
    }
}