package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.CartItem

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onCartItemUpdated: (CartItem) -> Unit,
    private val onCartItemRemoved: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)

        holder.increaseQuantity.setOnClickListener {
            cartItem.quantity++
            onCartItemUpdated(cartItem)
            notifyItemChanged(position)
        }

        holder.decreaseQuantity.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                onCartItemUpdated(cartItem)
                notifyItemChanged(position)
            }
        }

        holder.removeItem.setOnClickListener {
            onCartItemRemoved(cartItem)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.cartItemName)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.cartItemPrice)
        private val productQuantityTextView: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val increaseQuantity: Button = itemView.findViewById(R.id.increaseQuantity)
        val decreaseQuantity: Button = itemView.findViewById(R.id.decreaseQuantity)
        val removeItem: Button = itemView.findViewById(R.id.removeItem)

        fun bind(cartItem: CartItem) {
            itemNameTextView.text = cartItem.name
            itemPriceTextView.text = "Price: $${cartItem.price}"
            productQuantityTextView.text = "Qty: ${cartItem.quantity}"
        }
    }
}
