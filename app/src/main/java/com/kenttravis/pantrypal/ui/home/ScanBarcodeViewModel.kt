package com.kenttravis.pantrypal.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.remote.DetailProductResponse
import com.kenttravis.pantrypal.sources.remote.PostPantryRequest
import kotlinx.coroutines.launch

class ScanBarcodeViewModel(
    private val repo: PantryPalRepository
): ViewModel() {
    private val _data = MutableLiveData<DetailProductResponse>()
    val data: LiveData<DetailProductResponse>
        get() = _data

    fun getData(token: String, q: String, callback: (found: Boolean, product: DetailProductResponse?) -> Unit){
        viewModelScope.launch {
            try{
                val res = repo.searchProduct(token,q)
                _data.value = res
                callback.invoke(true, res)
            }catch(err: retrofit2.HttpException){
                Log.d("INFO","NOT FOUNDDD")
                callback.invoke(false,null)
            }
        }
    }

    fun addToPantry(token: String, callback: (success: Boolean)->Unit) {
        val data: PostPantryRequest = PostPantryRequest(_data.value)
        viewModelScope.launch {
            try {
                repo.addToPantry(token, data);
                callback.invoke(true);
            } catch (err: Exception) {
                callback.invoke(false);
            }
        }
    }

    fun setData(data: Map<String, Any>){
        Log.d("INFO", "DATAA"+data.toString())
        val detail: DetailProductResponse = DetailProductResponse(
            data.get("error").toString().toBoolean(),
            data["product_name"].toString(),
            data["ingredients"] as List<Map<String, Any>>,
            data["nutriments"] as  Map<String, Any>,
            data["grade"].toString(),
            data["missing"] as Map<String, Any>,
            data["allergens"].toString(),
            data["small_image_url"].toString(),
            data["image_url"].toString(),
            data["barcode"].toString()
        )
        _data.value = detail
    }
}