package com.kenttravis.pantrypal.sources.remote

interface RemoteDataSource {
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
    suspend fun deletePantryItem(token: String, id: String): ErrorAndMessageResponse
    suspend fun getUserInfo(token: String): UserInfoResponse
}