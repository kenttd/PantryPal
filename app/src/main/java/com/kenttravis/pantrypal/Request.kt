package com.kenttravis.pantrypal

data class AuthenticateRequest(
    val username: String,
    val password: String
)