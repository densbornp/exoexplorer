package com.burningstack.exoexplorer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.burningstack.exoexplorer.model.Planet

@Database(entities = [Planet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePlanetDao(): FavoritePlanetDao
}