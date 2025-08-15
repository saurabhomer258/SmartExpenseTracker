package com.example.smartexpensetracker.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.smartexpensetracker.domain.Category
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel


@Composable
fun EntryScreen(vm: ExpenseViewModel) {
    val ctx = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.Food) }
    var notes by remember { mutableStateOf("") }
    var receipt: Uri? by remember { mutableStateOf(null) }
    
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        receipt = uri
    }
    
    val todayTotal by vm.todayTotal.collectAsState()
    
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Total Spent Today: ₹${todayTotal}", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = title, onValueChange = { title = it.take(50) }, label = { Text("Title*") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = amountText,
            onValueChange = { if (it.all { ch -> ch.isDigit() }) amountText = it.take(8) },
            label = { Text("Amount (₹)*") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        CategoryDropdown(category) { category = it }
        OutlinedTextField(value = notes, onValueChange = { notes = it.take(100) }, label = { Text("Notes (opt)") }, modifier = Modifier.fillMaxWidth())
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { pickImage.launch("image/*") }) { Text("Add Receipt") }
            AnimatedVisibility(visible = receipt != null, enter = fadeIn(), exit = fadeOut()) {
                AsyncImage(model = receipt, contentDescription = null, modifier = Modifier.size(64.dp))
            }
        }
        
        Button(onClick = {
            val amt = amountText.toLongOrNull() ?: 0L
            if (title.isBlank() || amt <= 0L) {
                Toast.makeText(ctx, "Enter a valid title and amount", Toast.LENGTH_SHORT).show()
            } else {
                vm.addExpense(title, amt, category, notes.ifBlank { null }, receipt)
                title = ""; amountText = ""; notes = ""; receipt = null
                Toast.makeText(ctx, "Expense Added", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(selected: Category, onSelect: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Category.values().forEach { cat ->
                DropdownMenuItem(text = { Text(cat.name) }, onClick = { onSelect(cat); expanded = false })
            }
        }
    }
}