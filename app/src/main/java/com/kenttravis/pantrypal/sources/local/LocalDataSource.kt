package com.kenttravis.pantrypal.sources.local

import com.kenttravis.pantrypal.sources.local.entity.MealAreas
import com.kenttravis.pantrypal.sources.local.entity.MealCategories
import com.kenttravis.pantrypal.sources.local.entity.MealIngredients

interface LocalDataSource {
    suspend fun bulkInsertFilter(areas: List<MealAreas>, categories: List<MealCategories>, ingredients: List<MealIngredients>)
    suspend fun hasSavedFilter(): Boolean
    suspend fun getAreasFilter(): List<MealAreas>
    suspend fun getIngredientsFilter(): List<MealIngredients>
    suspend fun getCategoriesFilter(): List<MealCategories>
}