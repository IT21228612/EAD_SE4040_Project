package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val OrderId: String,                 // Order ID
    val customerId: String,              // ID of the customer who placed the order
    val products: List<Product>,          // Change this to a list of Product objects
    val isPurchased: Boolean,             // Whether the order has been purchased
    val status: String,                   // Order status (e.g., Pending, Shipped, Delivered)
    val createdAt: String                 // Date of order creation as a string (could be a Date type)
) : Parcelable
