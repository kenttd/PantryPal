package com.kenttravis.pantrypal.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kenttravis.pantrypal.sources.local.entity.MealAreas

@Dao
interface MealAreasDao {
    @Query("SELECT * FROM meal_areas")
    suspend fun getAll(): List<MealAreas>

    @Insert
    suspend fun insertAll(areas: MealAreas)

    @Insert
    suspend fun bulkInsert(areas: List<MealAreas>)

    @Query("SELECT count(strAreas) from meal_areas")
    suspend fun getTotal(): Int
}