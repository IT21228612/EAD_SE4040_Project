package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productId: String,      // Make sure this matches your API's field
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val isActive: Boolean,
    val vendorId: String,
    val vendorName: String
) : Parcelable
