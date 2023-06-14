package com.bangkit.fracturevision.model

data class User(
    val id: Int,
    val username: String,
    val fullname: String,
    val phone: String,
    val address: String,
    val isLogin: Boolean
)

data class LoginResponse(
    val address: String,
    val fullname: String,
    val id: Int,
    val phone: String,
    val username: String
)

data class FailedResponse(
    val error: Boolean,
    val message: String
)

data class RegisterResponse(
    val error: Boolean,
    val message: String
)
