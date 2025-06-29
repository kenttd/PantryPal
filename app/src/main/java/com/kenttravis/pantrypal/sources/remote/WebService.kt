package com.kenttravis.pantrypal.sources.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @POST("/authenticate")
    suspend fun authenticate(@Body post: AuthenticateRequest): ErrorAndMessageResponse

    @POST("/chat")
    suspend fun chat(@Header("authorization")token: String, @Body post: ChatRequest): ChatResponse

    @GET("/meal/random")
    suspend fun getRandomMeal(@Header("authorization")token: String): RecipeResponse

    @GET("/meal/data-filter")
    suspend fun getDataForFilter(@Header("authorization")token: String): DataFilterResponse

    @GET("/meal/filter/ingredient")
    suspend fun getIngredientFiltered(@Header("authorization")token: String, @Query("ingredient")ingredient: String): List<MealFilterResponse>

    @GET("/meal/filter/category")
    suspend fun getCategoryFiltered(@Header("authorization")token: String, @Query("category")category: String): List<MealFilterResponse>

    @GET("/meal/filter/area")
    suspend fun getAreaFiltered(@Header("authorization")token: String, @Query("area")area: String): List<MealFilterResponse>

    @GET("/meal/{id}")
    suspend fun getRecipeDetails(@Header("authorization")token: String,@Path("id")id: String): RecipeResponse

    @GET("/chat/history")
    suspend fun getChatHistory(@Header("authorization")token: String): ChatHistoryResponse

    @GET("/search")
    suspend fun searchProduct(@Header("authorization")token: String, @Query("q")q: String): DetailProductResponse

    @POST("/register")
    suspend fun register(@Body post: RegisterRequest): ErrorAndMessageResponse

    @GET("/chat/{id}")
    suspend fun getChatDetail(@Header("authorization")token: String,@Path("id")id: String): ChatDetailResponse

    @POST("/chat")
    suspend fun sendMessage(@Header("authorization")token: String, @Body post: SendMessageRequest): SendMessageResponse

    @POST("/chat/new")
    suspend fun sendMessageNew(@Header("authorization")token: String, @Body post: SendMessageRequest): SendMessageNewResponse

    @POST("/pantry")
    suspend fun addToPantry(@Header("authorization")token: String, @Body post: PostPantryRequest): ErrorAndMessageResponse

    @GET("/pantry")
    suspend fun getPantry(@Header("authorization")token: String): getPantryResponse

    @DELETE("/pantry/{id}")
    suspend fun deletePantryItem(@Header("authorization")token: String, @Path("id")id: String): ErrorAndMessageResponse

    @GET("/me")
    suspend fun getUserInfo(@Header("authorization")token: String): UserInfoResponse
}