package com.example.ecommerceapp.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.Product

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
    private val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
    private val productStocksTextView: TextView = itemView.findViewById(R.id.productStocksTextView)
    private val productCategoryTextView: TextView = itemView.findViewById(R.id.productCategoryTextView)
    private val productVendorTextView: TextView = itemView.findViewById(R.id.productVendorTextView)

    fun bind(product: Product) {
        productNameTextView.text = product.name
        productPriceTextView.text = "$${product.price}" // Format as needed
        productStocksTextView.text = "Stock: ${product.stock}" // Show stock
        productCategoryTextView.text = "Category: ${product.category ?: "Unavailable"}" // Show category
        productVendorTextView.text = "Vendor: ${product.vendorName ?: "Unavailable"}" // Show vendor name
    }
}
