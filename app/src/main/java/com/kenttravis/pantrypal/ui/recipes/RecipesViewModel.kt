package com.kenttravis.pantrypal.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.DataFilterResponse
import com.kenttravis.pantrypal.sources.remote.MealFilterResponse
import com.kenttravis.pantrypal.sources.remote.RecipeResponse
import kotlinx.coroutines.launch

class RecipesViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<DataFilterResponse>()
    val data: LiveData<DataFilterResponse>
        get() = _data

    private val _result = MutableLiveData<List<MealFilterResponse>>()
    val result: LiveData<List<MealFilterResponse>>
        get() = _result

    private val _recipe = MutableLiveData<RecipeResponse>()
    val recipe: LiveData<RecipeResponse>
        get() = _recipe

    fun getData(token: String){
        viewModelScope.launch {
            try {
                val res = repo.getDataFilter(token)
                _data.value = res
                Log.d("INFO","FILTERR"+res)
            }catch(err: Exception){
                Log.d("ERROR", err.toString())
            }
        }
    }

    fun getFilteredData(token: String, type: String, id: String){
        viewModelScope.launch {
            try {
                val res = repo.getFilteredResult(token, type, id)
                _result.value = res
            }catch(err: Exception){
                _result.value = emptyList()
            }
        }
    }

    fun getMeal(token: String, id: String){
        viewModelScope.launch {
            try {
                val res = repo.getRecipeDetails(token, id)
                _recipe.value = res
            }catch(err: Exception){

            }
        }
    }
}