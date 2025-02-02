package com.burningstack.exoexplorer.db

import android.content.Context
import androidx.room.Room

object AppDatabaseSingleton {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        // Check if an instance already exists, create one if not
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "exoexplorer_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}