package com.kenttravis.pantrypal.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.App
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.local.AuthManager
import com.kenttravis.pantrypal.sources.remote.AuthenticateRequest
import com.kenttravis.pantrypal.sources.remote.ErrorAndMessageResponse
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<ErrorAndMessageResponse>()
    val data: LiveData<ErrorAndMessageResponse>
        get() = _data

    fun authenticate(req: AuthenticateRequest){
        viewModelScope.launch {
            try{
                val res = repo.authenticate(req)
                _data.value = res
            }catch(err: retrofit2.HttpException){
//                val converter = App.retrofit
//                    .responseBodyConverter<ErrorAndMessageResponse>(
//                        ErrorAndMessageResponse::class.java,
//                        arrayOf()
//                    )
//                val parsedError = err.response()?.errorBody()?.let { converter.convert(it) }
//                _data.value = parsedError ?: ErrorAndMessageResponse(true, "Unknown error", null)

                //temporary solution
                _data.value = ErrorAndMessageResponse(true, "Login failed", null)
            }
        }
    }

    fun saveToken(token: String, context: Context){
        AuthManager.saveToken(context, token)
    }
}