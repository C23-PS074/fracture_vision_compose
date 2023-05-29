package com.bangkit.fracturevision.model

data class User(
    val id: Int,
    val username: String,
    val isLogin: Boolean
)

data class UserResponse(
    val id: Int,
    val username: String,
    val password: String,
    val nama: String
)

data class LoginResponse(
    val success: Boolean,
    val data: List<UserResponse>
)
