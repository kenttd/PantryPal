package com.kenttravis.pantrypal.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kenttravis.pantrypal.sources.local.dao.MealAreasDao
import com.kenttravis.pantrypal.sources.local.dao.MealCategoriesDao
import com.kenttravis.pantrypal.sources.local.dao.MealIngredientsDao
import com.kenttravis.pantrypal.sources.local.entity.MealAreas
import com.kenttravis.pantrypal.sources.local.entity.MealCategories
import com.kenttravis.pantrypal.sources.local.entity.MealIngredients

@Database(entities = [MealAreas::class, MealCategories::class, MealIngredients::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealAreasDao(): MealAreasDao
    abstract fun mealIngredientsDao(): MealIngredientsDao
    abstract fun mealCategoriesDao(): MealCategoriesDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "pantrypal"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}