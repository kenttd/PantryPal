package com.kenttravis.pantrypal

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {
    @POST("/authenticate")
    suspend fun authenticate(@Body post: AuthenticateRequest): ErrorAndMessageResponse
}