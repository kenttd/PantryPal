package com.kenttravis.pantrypal.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.ErrorAndMessageResponse
import com.kenttravis.pantrypal.sources.remote.RegisterRequest
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<ErrorAndMessageResponse>()
    val data: LiveData<ErrorAndMessageResponse>
        get() = _data

    fun register(req: RegisterRequest){
        viewModelScope.launch {
            try {
                val res = repo.register(req)
                _data.value = res
            }catch(err: Exception){
                _data.value = ErrorAndMessageResponse(true, "Failed to register", null)

            }
        }
    }
}