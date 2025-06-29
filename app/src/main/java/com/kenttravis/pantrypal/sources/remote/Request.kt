package com.kenttravis.pantrypal.sources.remote

data class AuthenticateRequest(
    val email: String,
    val password: String
)

data class ChatRequest(
    val isNew: Boolean,
    val message: String,
    val session_id: Int?
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirm_password: String,
    val dob: String,
    val email: String
)

data class SendMessageRequest(
    val session_id: String?,
    val message: String
)

data class PostPantryRequest(
    val data: Any
)