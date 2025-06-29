package com.kenttravis.pantrypal.ui.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.ChatDetailData
import com.kenttravis.pantrypal.sources.remote.ChatHistory
import com.kenttravis.pantrypal.sources.remote.SendMessageRequest
import kotlinx.coroutines.launch

class ChatbotViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<List<ChatHistory>>()
    val data: LiveData<List<ChatHistory>>
        get() = _data

    private val _detail = MutableLiveData<ChatDetailData>()
    val detail: LiveData<ChatDetailData>
        get() = _detail

    fun getHistory(token: String){
        viewModelScope.launch {
            try {
                val res = repo.getChatHistory(token)
                _data.value = res.data

            }catch(err: Exception){

            }
        }
    }

    fun getDetailChat(token: String, id: String){
        viewModelScope.launch {
            try {
                val res = repo.getChatDetail(token, id)
                _detail.value = res.data
                Log.d("INFO",res.toString())
            }catch(err: Exception){
                Log.d("INFO","ERRORR", err)
            }
        }
    }

    fun sendMessageNew(token: String, message: String,onFinish: (session_id: String)->Unit){
        viewModelScope.launch {
            try {
                val res = repo.sendMessageNew(token, SendMessageRequest(null,message))
                onFinish(res.session_id)
            }catch (err: Exception){

            }
        }
    }

    fun sendMessage(token: String, message: String, session_id: String, onFinish: () -> Unit){
        viewModelScope.launch {
            try {
                val res = repo.sendMessage(token, SendMessageRequest(session_id,message))
                onFinish()
            }catch (err: Exception){
                Log.d("INFO","ERRORR", err)
            }
        }
    }
}