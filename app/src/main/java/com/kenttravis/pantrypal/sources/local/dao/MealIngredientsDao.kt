package com.kenttravis.pantrypal.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kenttravis.pantrypal.sources.local.entity.MealIngredients

@Dao
interface MealIngredientsDao {
    @Insert
    suspend fun bulkInsert(ingredients: List<MealIngredients>)

    @Query("SELECT * FROM meal_ingredients")
    suspend fun getAll(): List<MealIngredients>

    @Query("SELECT count(idIngredient) from meal_ingredients")
    suspend fun getTotal(): Int
}