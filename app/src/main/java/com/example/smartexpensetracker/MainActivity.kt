package com.example.smartexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartexpensetracker.nav.Route
import com.example.smartexpensetracker.ui.screen.EntryScreen
import com.example.smartexpensetracker.ui.screen.ListScreen
import com.example.smartexpensetracker.ui.screen.ReportScreen
import com.example.smartexpensetracker.ui.theme.SmartExpenseTheme
import com.example.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var dark by remember { mutableStateOf(false) }
            SmartExpenseTheme(dark = dark) {
                val nav = rememberNavController()
                val vm = remember { ExpenseViewModel() }
                Scaffold(topBar = {
                    TopAppBar(title = { Text("Smart Expense Tracker") }, actions = {
                        Row {
                            TextButton(onClick = { nav.navigate(Route.Entry.path) }) { Text("Entry") }
                            TextButton(onClick = { nav.navigate(Route.List.path) }) { Text("List") }
                            TextButton(onClick = { nav.navigate(Route.Report.path) }) { Text("Report") }
                            IconButton(onClick = { dark = !dark }) { Icon(Icons.Default.Create, contentDescription = "Theme") }
                        }
                    })
                }) { padding ->
                    NavHost(navController = nav, startDestination = Route.Entry.path, modifier = androidx.compose.ui.Modifier.padding(padding)) {
                        composable(Route.Entry.path) { EntryScreen(vm) }
                        composable(Route.List.path) { ListScreen(vm) }
                        composable(Route.Report.path) { ReportScreen(vm) }
                    }
                }
            }
        }
    }
}