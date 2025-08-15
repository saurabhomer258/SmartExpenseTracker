package com.example.smartexpensetracker.data

import com.example.smartexpensetracker.domain.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    val expenses: Flow<List<Expense>>
    suspend fun add(expense: Expense)
    suspend fun remove(id: String)
    suspend fun clear()
}