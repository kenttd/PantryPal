package com.kenttravis.pantrypal.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_categories")
data class MealCategories (
    @PrimaryKey(autoGenerate = false)
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)