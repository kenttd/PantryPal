package com.kenttravis.pantrypal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.DetailProductResponse
import com.kenttravis.pantrypal.sources.remote.getPantryResponse
import kotlinx.coroutines.launch

class PantryViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<getPantryResponse>()
    val data: LiveData<getPantryResponse>
        get() = _data

    fun getPantryData(token: String){
        viewModelScope.launch {
            try {
                val res = repo.getPantry(token)
                _data.value = res
            }catch (err: Exception){

            }
        }
    }

    fun deletePantryItem(token: String, id: String){
        viewModelScope.launch {
            try {
                repo.deleteItemPantry(token, id)
            }catch(err: Exception){

            }
        }
    }
}