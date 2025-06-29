package com.kenttravis.pantrypal.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.UserInfoResponse
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<UserInfoResponse>()
    val data: LiveData<UserInfoResponse>
        get() = _data
    fun getData(token: String){
        viewModelScope.launch {
            try {
                val res = repo.getUserInfo(token)
                _data.value = res
                Log.d("INFO",res.toString())
            }catch(err: Exception){
                Log.d("INFO",err.toString())
            }
        }
    }
}