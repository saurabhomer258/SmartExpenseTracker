package com.example.smartexpensetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Light = lightColorScheme()
private val Dark = darkColorScheme()

@Composable
fun SmartExpenseTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) { MaterialTheme(colorScheme = if (dark) Dark else Light, content = content) }