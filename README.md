# Smart Daily Expense Tracker

## 📌 Overview
**Smart Daily Expense Tracker** is a modern Android application built with **Jetpack Compose** and **MVVM architecture**.  
It helps small business owners record, view, and analyze daily expenses quickly and efficiently.  
Optional mock AI-assisted workflows make the process smarter and more intuitive.

---

## ✨ Features
- **Expense Entry Screen** – Add title, amount, category, notes, and optional receipt image.
- **Real-time Today Total** – Live calculation of today’s total spending.
- **Expense List Screen** – Filter by date, group by category, view count & total, and delete expenses.
- **Expense Report Screen** – View last 7 days' daily totals, category-wise totals, and a simple bar chart.
- **Export CSV & Share** – Generate a CSV report and share via Android’s share sheet.
- **Theme Switcher** – Toggle between Light and Dark modes.
- **Validation** – Prevent empty titles or non-positive amounts.
- **Duplicate Detection** – Avoid adding the same expense within 2 minutes.
- **Mock Offline Sync** – Simulate network sync for offline testing.

---

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **State Management**: StateFlow
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **File Sharing**: FileProvider

---

## 📦 Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/smart-daily-expense-tracker.git
    ```
2. Open in **Android Studio** (latest stable version recommended).
3. Build and run on an emulator or physical device.

---

## 🚀 Usage

### **Adding an Expense**
```kotlin
val context = LocalContext.current
Button(onClick = {
    vm.addExpense(
        title = "Tea for staff",
        amount = 50,
        category = Category.Food,
        notes = "Morning refreshment",
        receipt = null
    )
    Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show()
}) {
    Text("Add Expense")
}
```

---

### **Viewing Expenses**
```kotlin
val expenses by vm.allExpenses.collectAsState()
LazyColumn {
    items(expenses) { expense ->
        Text("${expense.title}: ₹${expense.amount} — ${expense.category}")
    }
}
```

**Features:**
- View today’s expenses or navigate to other dates.
- Toggle between grouping by category or showing a flat list.
- Display total count & amount for the selected date.

---

### **Filtering by Date**
```kotlin
var selectedDate by remember { mutableStateOf(LocalDate.now()) }
Button(onClick = { selectedDate = selectedDate.minusDays(1) }) { Text("Previous") }
Button(onClick = { selectedDate = selectedDate.plusDays(1) }) { Text("Next") }
```

---

### **Grouping by Category**
```kotlin
val grouped = expenses.groupBy { it.category }
grouped.forEach { (category, items) ->
    Text(category.name, style = MaterialTheme.typography.titleMedium)
    items.forEach { exp ->
        Text("${exp.title}: ₹${exp.amount}")
    }
}
```

---

### **Exporting CSV**
```kotlin
val context = LocalContext.current
Button(onClick = { exportCsvAndShare(context, dailyTotals) }) {
    Text("Export CSV & Share")
}
```

---

## 🔀 Navigation Setup
```kotlin
NavHost(navController = nav, startDestination = Route.Entry.path) {
    composable(Route.Entry.path) { EntryScreen(vm) }
    composable(Route.List.path) { ListScreen(vm) }
    composable(Route.Report.path) { ReportScreen(vm) }
}
```

---

## 📊 Report Screen Example
Displays:
- **Daily Totals** – Last 7 days.
- **Category Totals** – Last 7 days.
- **Bar Chart** – Simple visual representation.

```kotlin
val dailyTotals = mapOf(
    LocalDate.now() to 250L,
    LocalDate.now().minusDays(1) to 400L
)
BarChart(values = dailyTotals.values.map { it.toFloat() })
```

---

## 🤖 AI Usage Summary
AI tools were used to:
- Generate initial Compose UI layout ideas.
- Draft MVVM structure & ViewModel/Data classes.
- Suggest UX improvements.
- Create validation & duplicate detection logic.
- Write README and in-code comments.

---

## 📄 License
This project is licensed under the **MIT License**.
