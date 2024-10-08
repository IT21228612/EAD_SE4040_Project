package com.example.ecommerceapp.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.CartItem

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemNameTextView: TextView = itemView.findViewById(R.id.cartItemName) // Use correct ID
    private val itemPriceTextView: TextView = itemView.findViewById(R.id.cartItemPrice) // Use correct ID
    val productQuantityTextView: TextView = itemView.findViewById(R.id.cartItemQuantity) // Quantity TextView
    val increaseQuantity: Button = itemView.findViewById(R.id.increaseQuantity)
    val decreaseQuantity: Button = itemView.findViewById(R.id.decreaseQuantity)
    val removeItem: Button = itemView.findViewById(R.id.removeItem)

    fun bind(cartItem: CartItem) {
        itemNameTextView.text = cartItem.name // Set item name
        itemPriceTextView.text = "Price: $${cartItem.price}" // Set item price
        productQuantityTextView.text = "Qty: ${cartItem.quantity}" // Set item quantity
    }
}
