package com.kenttravis.pantrypal.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_ingredients")
data class MealIngredients(
    @PrimaryKey(autoGenerate = false)
    val idIngredient: String,
    val strIngredients: String,
    val strDescription: String?,
    val strType: String?
)