package com.example.ecommerceapp

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface EcommerceService {
    @POST("api/accounts")
    suspend fun createAccount(@Body accountRequest: AccountRequest): Response<Void>

    @POST("api/accounts/login")
    suspend fun login(@Body accountRequest: AccountRequest): Response<Void>

    @POST("/api/accounts/approve")
    suspend fun approveAccount(@Body email: String): Response<Void>

    @POST("/api/accounts/reactivate")
    suspend fun reactivateAccount(@Body email: String): Response<Void>

    // Reject account
    @POST("/api/accounts/reject")
    suspend fun rejectAccount(@Body email: String): Response<Void>



    @POST("api/feedback")
    suspend fun submitFeedback(@Body feedback: Feedback): Response<Void>
}

