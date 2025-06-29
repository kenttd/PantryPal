package com.kenttravis.pantrypal.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kenttravis.pantrypal.sources.local.entity.MealCategories

@Dao
interface MealCategoriesDao {
    @Insert
    suspend fun bulkInsert(areas: List<MealCategories>)

    @Query("SELECT * FROM meal_categories")
    suspend fun getAll(): List<MealCategories>

    @Query("SELECT count(idCategory) from meal_categories")
    suspend fun getTotal(): Int
}