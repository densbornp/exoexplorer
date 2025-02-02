package com.burningstack.exoexplorer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.burningstack.exoexplorer.model.Planet

@Dao
interface FavoritePlanetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(planet: Planet)

    @Delete
    suspend fun removeFavorite(planet: Planet)

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites(): List<Planet>

    @Query("SELECT * FROM favorites WHERE name LIKE '%' || :planetId || '%'")
    suspend fun getFavorites(planetId: String): List<Planet>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE name=:planetId)")
    suspend fun isFavorite(planetId: String): Boolean
}