package com.example.ecommerceapp.network

import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.Product
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/product") // Endpoint to get all products
    fun getAllProducts(): Call<List<Product>>

    @GET("api/product/{id}")
    fun getProductById(@Path("id") productId: String): Call<Product>

    @GET("api/cart") // Fetch all cart items for the current user
    fun getCartItems(): Call<List<CartItem>>

    @POST("api/cart") // Endpoint to add item to cart
    fun addToCart(@Body cartItem: CartItem): Call<CartItem>

    @PUT("api/cart/{id}") // Endpoint to update cart item
    fun updateCartItem(@Path("id") id: String, @Body cartItem: CartItem): Call<CartItem>

    @DELETE("api/cart/{id}") // Endpoint to remove cart item
    fun removeCartItem(@Path("id") id: String): Call<Void>

    @POST("api/order/create") // Endpoint to create order
    fun createOrder(@Body order: Order): Call<Order>

    @PUT("api/order/update/{orderId}") // Endpoint to update order
    fun updateOrder(@Path("orderId") orderId: String, @Body order: Order): Call<Order>

    @DELETE("api/order/cancel/{orderId}") // Endpoint to cancel order
    fun cancelOrder(@Path("orderId") orderId: String): Call<Void>

    @GET("api/order/customer/{customerId}")
    fun getOrdersByCustomer(@Path("customerId") customerId: String): Call<List<Order>>


    @GET("api/order/{orderId}") // Endpoint to get order by ID
    fun getOrderById(@Path("orderId") orderId: String): Call<Order>

    @GET("api/order/all") // Endpoint to get all orders
    fun getAllOrders(): Call<List<Order>>


    companion object {
        private const val BASE_URL = "http://10.0.2.2:5177" // For emulator (or replace with your computer’s IP if on physical device)

        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}
