package com.kenttravis.pantrypal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _data = MutableLiveData<ErrorAndMessageResponse>()
    val data: LiveData<ErrorAndMessageResponse>
        get() = _data

    fun authenticate(req: AuthenticateRequest){
        viewModelScope.launch {
            val res = App.retrofitService.authenticate(req)
            _data.value = res
        }
    }
}