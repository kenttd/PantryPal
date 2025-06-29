package com.kenttravis.pantrypal.repository

import com.kenttravis.pantrypal.sources.remote.AuthenticateRequest
import com.kenttravis.pantrypal.sources.remote.ChatDetailResponse
import com.kenttravis.pantrypal.sources.remote.ChatHistoryResponse
import com.kenttravis.pantrypal.sources.remote.DataFilterResponse
import com.kenttravis.pantrypal.sources.remote.DetailProductResponse
import com.kenttravis.pantrypal.sources.remote.ErrorAndMessageResponse
import com.kenttravis.pantrypal.sources.remote.MealFilterResponse
import com.kenttravis.pantrypal.sources.remote.PostPantryRequest
import com.kenttravis.pantrypal.sources.remote.RecipeResponse
import com.kenttravis.pantrypal.sources.remote.RegisterRequest
import com.kenttravis.pantrypal.sources.remote.SendMessageNewResponse
import com.kenttravis.pantrypal.sources.remote.SendMessageRequest
import com.kenttravis.pantrypal.sources.remote.SendMessageResponse
import com.kenttravis.pantrypal.sources.remote.UserInfoResponse
import com.kenttravis.pantrypal.sources.remote.getPantryResponse

interface PantryPalRepository {
    suspend fun authenticate(req: AuthenticateRequest): ErrorAndMessageResponse
    suspend fun searchProduct(token: String, q: String): DetailProductResponse
    suspend fun register(req: RegisterRequest): ErrorAndMessageResponse
    suspend fun getDataFilter(token: String): DataFilterResponse
    suspend fun getFilteredResult(token: String, type: String, id: String): List<MealFilterResponse>
    suspend fun getRecipeDetails(token: String, id: String): RecipeResponse
    suspend fun getChatHistory(token: String): ChatHistoryResponse
    suspend fun getChatDetail(token: String, id: String): ChatDetailResponse
    suspend fun sendMessage(token: String, req: SendMessageRequest): SendMessageResponse
    suspend fun sendMessageNew(token: String, req: SendMessageRequest): SendMessageNewResponse
    suspend fun addToPantry(token: String, req: PostPantryRequest): ErrorAndMessageResponse
    suspend fun getPantry(token: String): getPantryResponse
    suspend fun deleteItemPantry(token: String, id: String): ErrorAndMessageResponse
    suspend fun getUserInfo(token: String): UserInfoResponse
}