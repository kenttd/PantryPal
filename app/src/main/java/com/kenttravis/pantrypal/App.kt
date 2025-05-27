package com.kenttravis.pantrypal

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App: Application() {
    companion object{
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl("http://192.168.18.52:3002/").build()
        val retrofitService = retrofit.create(WebService::class.java)
    }
    override fun onCreate() {
        super.onCreate()
    }
}