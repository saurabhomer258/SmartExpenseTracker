# Smart Daily Expense Tracker — README

## Overview
This is a full-featured **Smart Daily Expense Tracker** built with **Jetpack Compose** and **MVVM** architecture. It allows small business owners to record, view, and analyze daily expenses quickly and efficiently, with optional mock AI-assisted workflows.

## Features
- **Expense Entry Screen**: Add title, amount, category, notes, and optional receipt image.
- **Real-time Today Total**: Live calculation of today's total spending.
- **Expense List Screen**: Filter by date, group by category, view count and total, delete expenses.
- **Expense Report Screen**: Last 7 days daily totals, category-wise totals, bar chart visualization.
- **Export CSV & Share**: Generate a CSV report and share via Android's share sheet.
- **Theme Switcher**: Light/Dark mode toggle.
- **Validation**: Prevent empty title or non-positive amounts.
- **Duplicate Detection**: Avoid adding the same expense within 2 minutes.
- **Mock Offline Sync**: Simulated network sync.

## Tech Stack
- **Kotlin**
- **Jetpack Compose**
- **StateFlow** for state management
- **Navigation Compose** for screen navigation
- **Coil** for image loading
- **FileProvider** for file sharing

## Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/smart-daily-expense-tracker.git

