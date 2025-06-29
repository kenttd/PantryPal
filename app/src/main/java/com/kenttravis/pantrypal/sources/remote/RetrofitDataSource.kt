package com.kenttravis.pantrypal.sources.remote

class RetrofitDataSource(
    private val api: WebService
): RemoteDataSource {
    override suspend fun authenticate(req: AuthenticateRequest): ErrorAndMessageResponse {
        return api.authenticate(req)
    }

    override suspend fun searchProduct(token: String, q: String): DetailProductResponse {
        return api.searchProduct(token, q)
    }

    override suspend fun register(req: RegisterRequest): ErrorAndMessageResponse {
        return api.register(req)
    }

    override suspend fun getDataFilter(token: String): DataFilterResponse {
        return api.getDataForFilter(token)
    }

    override suspend fun getFilteredResult(
        token: String,
        type: String,
        id: String
    ): List<MealFilterResponse> {
        when(type){
            "areas"->return api.getAreaFiltered(token, id)
            "ingredients"->return api.getIngredientFiltered(token, id)
            "categories"->return api.getCategoryFiltered(token, id)
            else->return emptyList<MealFilterResponse>()
        }
    }

    override suspend fun getRecipeDetails(token: String, id: String): RecipeResponse {
        return api.getRecipeDetails(token, id)
    }

    override suspend fun getChatHistory(token: String): ChatHistoryResponse {
        return api.getChatHistory(token)
    }

    override suspend fun sendMessage(token: String, req: SendMessageRequest): SendMessageResponse {
        return api.sendMessage(token, req)
    }

    override suspend fun sendMessageNew(
        token: String,
        req: SendMessageRequest
    ): SendMessageNewResponse {
        return api.sendMessageNew(token, req)
    }

    override suspend fun getChatDetail(token: String, id: String): ChatDetailResponse {
        return api.getChatDetail(token, id)
    }

    override suspend fun addToPantry(
        token: String,
        req: PostPantryRequest
    ): ErrorAndMessageResponse {
        return api.addToPantry(token, req)
    }

    override suspend fun getPantry(token: String): getPantryResponse {
        return api.getPantry(token)
    }

    override suspend fun deletePantryItem(token: String, id: String): ErrorAndMessageResponse {
        return api.deletePantryItem(token, id)
    }

    override suspend fun getUserInfo(token: String): UserInfoResponse {
        return api.getUserInfo(token)
    }
}