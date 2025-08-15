package com.example.smartexpensetracker.data

import com.example.smartexpensetracker.domain.Expense


import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryExpenseRepository : ExpenseRepository {
    private val mutex = Mutex()
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    override val expenses = _expenses.asStateFlow()
    
    override suspend fun add(expense: Expense) {
        mutex.withLock {
            val list = _expenses.value.toMutableList()
            // Duplicate detection: same title+amount+date within 2 minutes
            val exists = list.any {
                it.title.equals(expense.title, ignoreCase = true) &&
                    it.amount == expense.amount &&
                    it.category == expense.category &&
                    kotlin.runCatching { java.time.Duration.between(it.dateTime, expense.dateTime).abs().toMinutes() }
                        .getOrDefault(999) <= 2
            }
            if (exists) {
                Log.d("ExpenseRepo", "Duplicate blocked: ${expense.title} ₹${expense.amount}")
                return
            }
            list.add(expense)
            _expenses.value = list.sortedByDescending { it.dateTime }
            Log.d("ExpenseRepo", "Added: ${expense.title} amount=${expense.amount}")
        }
    }
    
    override suspend fun remove(id: String) {
        mutex.withLock { _expenses.value = _expenses.value.filterNot { it.id == id } }
    }
    
    override suspend fun clear() { mutex.withLock { _expenses.value = emptyList() } }
}