package com.example.smartexpensetracker.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartexpensetracker.data.ExpenseRepository
import com.example.smartexpensetracker.data.InMemoryExpenseRepository
import com.example.smartexpensetracker.data.SyncManager
import com.example.smartexpensetracker.domain.Category
import com.example.smartexpensetracker.domain.Expense
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

open class ExpenseViewModel(
    private val repo: ExpenseRepository = InMemoryExpenseRepository()
) : ViewModel() {
    
    val allExpenses: StateFlow<List<Expense>> = repo.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    
    val todayTotal: StateFlow<Long> = repo.expenses
        .map { list ->
            val today = LocalDate.now()
            list.filter { it.localDate == today }.sumOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)
    
    fun addExpense(
        title: String,
        amount: Long,
        category: Category,
        notes: String?,
        receipt: Uri?
    ) {
        if (title.isBlank() || amount <= 0L) return
        viewModelScope.launch {
            repo.add(
                Expense(
                    title = title.trim(),
                    amount = amount,
                    category = category,
                    notes = notes?.take(100),
                    receiptUri = receipt,
                    dateTime = LocalDateTime.now()
                )
            )
        }
    }
    
    fun removeExpense(id: String) { viewModelScope.launch { repo.remove(id) } }
    
    fun clearAll() { viewModelScope.launch { repo.clear() } }
    
    fun sync() { viewModelScope.launch { SyncManager.syncNow() } }
}