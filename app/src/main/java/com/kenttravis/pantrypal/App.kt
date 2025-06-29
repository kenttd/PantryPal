package com.kenttravis.pantrypal

import android.app.Application
import com.kenttravis.pantrypal.repository.PantryPalDefaultRepository
import com.kenttravis.pantrypal.repository.PantryPalRepository
import com.kenttravis.pantrypal.sources.local.AppDatabase
import com.kenttravis.pantrypal.sources.local.RoomDataSource
import com.kenttravis.pantrypal.sources.remote.RetrofitDataSource
import com.kenttravis.pantrypal.sources.remote.WebService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App: Application() {
    lateinit var repo: PantryPalRepository
    override fun onCreate() {
        super.onCreate()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl("https://pantrypal.kentdonovan.com").build()
        val retrofitService = retrofit.create(WebService::class.java)
        repo = PantryPalDefaultRepository(
            RoomDataSource(AppDatabase.getInstance(baseContext)),
            RetrofitDataSource(retrofitService)
        )
    }
}