package com.kenttravis.pantrypal.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "meal_areas")
data class MealAreas (
    @PrimaryKey(autoGenerate = false)
    val strAreas: String
)