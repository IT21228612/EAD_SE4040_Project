package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.Order

class OrderAdapter(
    private var orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        private val orderStatusTextView: TextView = itemView.findViewById(R.id.orderStatusTextView)
        private val orderProductsTextView: TextView = itemView.findViewById(R.id.orderProductsTextView)

        fun bind(order: Order) {
            orderIdTextView.text = "Order ID: ${order.customerId}"
            orderStatusTextView.text = "Status: ${order.status}"

            // Display product details
            val productDetails = order.products.joinToString("\n") { product ->
                "${product.name} - ${product.category} - \$${product.price}"
            }
            orderProductsTextView.text = productDetails
        }
    }
}