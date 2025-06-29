package com.kenttravis.pantrypal.repository

import com.kenttravis.pantrypal.sources.local.LocalDataSource
import com.kenttravis.pantrypal.sources.local.entity.MealAreas
import com.kenttravis.pantrypal.sources.local.entity.MealCategories
import com.kenttravis.pantrypal.sources.local.entity.MealIngredients
import com.kenttravis.pantrypal.sources.remote.Area
import com.kenttravis.pantrypal.sources.remote.AuthenticateRequest
import com.kenttravis.pantrypal.sources.remote.Category
import com.kenttravis.pantrypal.sources.remote.ChatDetailResponse
import com.kenttravis.pantrypal.sources.remote.ChatHistoryResponse
import com.kenttravis.pantrypal.sources.remote.DataFilterResponse
import com.kenttravis.pantrypal.sources.remote.DetailProductResponse
import com.kenttravis.pantrypal.sources.remote.ErrorAndMessageResponse
import com.kenttravis.pantrypal.sources.remote.Ingredient
import com.kenttravis.pantrypal.sources.remote.MealFilterResponse
import com.kenttravis.pantrypal.sources.remote.PostPantryRequest
import com.kenttravis.pantrypal.sources.remote.RecipeResponse
import com.kenttravis.pantrypal.sources.remote.RegisterRequest
import com.kenttravis.pantrypal.sources.remote.RemoteDataSource
import com.kenttravis.pantrypal.sources.remote.SendMessageNewResponse
import com.kenttravis.pantrypal.sources.remote.SendMessageRequest
import com.kenttravis.pantrypal.sources.remote.SendMessageResponse
import com.kenttravis.pantrypal.sources.remote.UserInfoResponse
import com.kenttravis.pantrypal.sources.remote.getPantryResponse

class PantryPalDefaultRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): PantryPalRepository {
    override suspend fun authenticate(req: AuthenticateRequest): ErrorAndMessageResponse {
        return remoteDataSource.authenticate(req)
    }

    override suspend fun searchProduct(token: String, q: String): DetailProductResponse {
        return remoteDataSource.searchProduct(token,q)
    }

    override suspend fun register(req: RegisterRequest): ErrorAndMessageResponse {
        return remoteDataSource.register(req)
    }

    override suspend fun getDataFilter(token: String): DataFilterResponse {
        if(localDataSource.hasSavedFilter()){
            val areas = localDataSource.getAreasFilter()
            val ingredients = localDataSource.getIngredientsFilter()
            val categories = localDataSource.getCategoriesFilter()
            return DataFilterResponse(areas.map{Area(it.strAreas)},categories.map{ Category(it.idCategory,it.strCategory,it.strCategoryThumb,it.strCategoryDescription) },ingredients.map{Ingredient(it.idIngredient,it.strIngredients,it.strDescription,it.strType)})
        }else{
            val res = remoteDataSource.getDataFilter(token)
            localDataSource.bulkInsertFilter(res.areas.map{MealAreas(it.strArea)},res.categories.map{MealCategories(it.idCategory, it.strCategory,it.strCategoryThumb,it.strCategoryDescription)},res.ingredients.map{ MealIngredients(it.idIngredient,it.strIngredient,it.strDescription,it.strType)})
            return res
        }
    }

    override suspend fun getFilteredResult(
        token: String,
        type: String,
        id: String
    ): List<MealFilterResponse> {
        return remoteDataSource.getFilteredResult(token, type, id)
    }

    override suspend fun getRecipeDetails(token: String, id: String): RecipeResponse {
        return remoteDataSource.getRecipeDetails(token, id)
    }

    override suspend fun getChatHistory(token: String): ChatHistoryResponse {
        return remoteDataSource.getChatHistory(token)
    }

    override suspend fun getChatDetail(token: String, id: String): ChatDetailResponse {
        return remoteDataSource.getChatDetail(token, id)
    }

    override suspend fun sendMessage(token: String, req: SendMessageRequest): SendMessageResponse {
        return remoteDataSource.sendMessage(token, req)
    }

    override suspend fun sendMessageNew(
        token: String,
        req: SendMessageRequest
    ): SendMessageNewResponse {
        return remoteDataSource.sendMessageNew(token, req)
    }

    override suspend fun addToPantry(
        token: String,
        req: PostPantryRequest
    ): ErrorAndMessageResponse {
        return remoteDataSource.addToPantry(token, req)
    }

    override suspend fun getPantry(token: String): getPantryResponse {
        return remoteDataSource.getPantry(token)
    }

    override suspend fun deleteItemPantry(token: String, id: String): ErrorAndMessageResponse {
        return remoteDataSource.deletePantryItem(token, id)
    }

    override suspend fun getUserInfo(token: String): UserInfoResponse {
        return remoteDataSource.getUserInfo(token)
    }
}