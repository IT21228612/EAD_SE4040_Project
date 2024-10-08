package com.example.ecommerceapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.adapters.OrderAdapter
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityOrderTrackingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var binding: ActivityOrderTrackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()

        orderAdapter = OrderAdapter(listOf()) { order ->
            // Handle order click to show details or perform other actions
        }

        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = orderAdapter

        fetchAllOrders() // Call the new fetch method
    }

    private fun fetchAllOrders() {
        apiService.getAllOrders().enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    orderAdapter.updateOrders(orders)
                } else {
                    Toast.makeText(this@OrderTrackingActivity, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Toast.makeText(this@OrderTrackingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
