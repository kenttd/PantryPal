package com.kenttravis.pantrypal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _data = MutableLiveData<List<String>>()
    val data: LiveData<List<String>>
        get() = _data

    fun getData(){

    }
}
