package com.example.ecommerceapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityBrowseProductsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrowseProductsActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityBrowseProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()

        productAdapter = ProductAdapter(listOf()) { product ->
            // Handle product click
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("productId", product.productId)
            startActivity(intent)
        }

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = productAdapter

        fetchProducts()
    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    productAdapter.updateProducts(response.body() ?: emptyList())
                } else {
                    // Log the error response code and message
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@BrowseProductsActivity, "Error: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@BrowseProductsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
