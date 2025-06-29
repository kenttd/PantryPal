package com.kenttravis.pantrypal.sources.remote

import com.example.userprofile.User
import com.squareup.moshi.JsonClass

data class ErrorAndMessageResponse(
    val error: Boolean,
    val message: String,
    val token: String?
)

data class ChatResponse(
    val error: Boolean,
    val message: String?,
    val data: String
)

data class RecipeResponse(
    val idMeal: String,
    val strMeal: String,
    val strMealAlternate: String?,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strTags: String?,
    val strYoutube: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient20: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure20: String?,
    val strSource: String?,
    val strImageSource: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?
)

data class DataFilterResponse(
    val areas: List<Area>,
    val categories: List<Category>,
    val ingredients: List<Ingredient>
)

data class MealFilterResponse(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
)

data class ChatHistoryResponse(
    val error: Boolean,
    val data: List<ChatHistory>
)

@JsonClass(generateAdapter = true)
data class DetailProductResponse(
    val error: Boolean,
    val product_name: String?,
    val ingredients: List<Map<String, Any>>?,
    val nutriments: Map<String, Any>?,
    val grade: String?,
    val missing: Map<String, Any>?,
    val allergens: String?,
    val small_image_url: String?,
    val image_url: String?,
    val barcode: String?
)

data class ChatHistory(
    val id: Int,
    val user_id: Int,
    val title: String,
    val started_at: String,
    val updated_at: String,
    val last_message: String?
)


data class Area(
    val strArea: String
)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class Ingredient(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String?,
    val strType: String?
)

data class ChatDetailResponse(
    val error: Boolean,
    val data: ChatDetailData
)

data class ChatDetailData(
    val session: ChatHistory,
    val messages: List<ChatDetail>
)

data class ChatDetail(
    val id: Int,
    val session_id: Int,
    val sender: String,
    val message: String,
    val timestamp: String
)

data class SendMessageResponse(
    val error: Boolean,
    val data: String
)

data class SendMessageNewResponse(
    val error: Boolean,
    val session_id: String
)

data class getPantryResponse(
    val error: Boolean,
    val data: List<Map<String, Any>>
)

data class UserInfoResponse(
    val error: Boolean,
    val user: User
)

