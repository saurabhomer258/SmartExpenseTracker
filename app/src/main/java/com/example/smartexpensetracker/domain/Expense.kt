package com.example.smartexpensetracker.domain

import android.net.Uri
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

enum class Category { Staff, Travel, Food, Utility }
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Long, // stored in paise for precision if needed; here using ₹ as long
    val category: Category,
    val notes: String? = null,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val receiptUri: Uri? = null
) {
    val localDate: LocalDate get() = dateTime.toLocalDate()
}