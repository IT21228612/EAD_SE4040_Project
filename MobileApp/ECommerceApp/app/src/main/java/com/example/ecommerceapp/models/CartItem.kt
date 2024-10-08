package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: String,          // Unique identifier (could be ObjectId converted to String)
    val productId: String,   // Product ID
    var quantity: Int,       // Quantity of the product
    val userId: String,      // User ID
    val name: String,        // Name of the product
    val price: Double        // Price of the product
) : Parcelable
