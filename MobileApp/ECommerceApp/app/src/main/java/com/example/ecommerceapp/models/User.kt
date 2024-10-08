package com.example.ecommerceapp.models

data class User(
    val id: String,
    val email: String,
    val password: String,
    val isApproved: Boolean
)
