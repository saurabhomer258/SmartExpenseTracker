package com.example.smartexpensetracker.data

import android.util.Log
import kotlinx.coroutines.delay

object SyncManager {
    // Pretend to push/pull from server
    suspend fun syncNow(): Boolean {
        Log.d("SyncManager", "Sync started")
        delay(800) // simulate network
        Log.d("SyncManager", "Sync completed")
        return true
    }
}