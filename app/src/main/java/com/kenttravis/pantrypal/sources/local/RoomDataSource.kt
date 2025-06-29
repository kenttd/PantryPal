package com.kenttravis.pantrypal.sources.local

import com.kenttravis.pantrypal.sources.local.entity.MealAreas
import com.kenttravis.pantrypal.sources.local.entity.MealCategories
import com.kenttravis.pantrypal.sources.local.entity.MealIngredients

class RoomDataSource(
    private val db: AppDatabase
): LocalDataSource {
    override suspend fun bulkInsertFilter(
        areas: List<MealAreas>,
        categories: List<MealCategories>,
        ingredients: List<MealIngredients>
    ) {
        db.mealAreasDao().bulkInsert(areas)
        db.mealCategoriesDao().bulkInsert(categories)
        db.mealIngredientsDao().bulkInsert(ingredients)
    }

    override suspend fun hasSavedFilter(): Boolean {
        return db.mealCategoriesDao().getTotal()>0 && db.mealIngredientsDao().getTotal()>0 && db.mealAreasDao().getTotal()>0
    }

    override suspend fun getAreasFilter(): List<MealAreas> {
        return db.mealAreasDao().getAll()
    }

    override suspend fun getCategoriesFilter(): List<MealCategories> {
        return db.mealCategoriesDao().getAll()
    }

    override suspend fun getIngredientsFilter(): List<MealIngredients> {
        return db.mealIngredientsDao().getAll()
    }
}