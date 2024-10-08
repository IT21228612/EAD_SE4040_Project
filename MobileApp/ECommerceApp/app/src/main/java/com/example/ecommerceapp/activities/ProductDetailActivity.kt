package com.example.ecommerceapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityProductDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var apiService: ApiService
    private lateinit var productId: String
    private var product: Product? = null // Store the product details here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()
        productId = intent.getStringExtra("productId") ?: ""

        // Log the Product ID to verify it
        Log.d("ProductDetailActivity", "Product ID: $productId")

        // Fetch the product details based on the productId passed from the previous activity
        fetchProductDetails()

        // Handle Add to Cart button click
        binding.addToCartButton.setOnClickListener {
            addToCart() // Call the function to add the product to the cart
        }
    }

    // Function to fetch product details
    private fun fetchProductDetails() {
        Log.d("ProductDetailActivity", "Fetching details for Product ID: $productId")

        apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                Log.d("ProductDetailActivity", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    product = response.body() // Store the fetched product
                    product?.let { prod ->
                        Log.d("ProductDetailActivity", "Product details: $prod")
                        // Populate views with product data
                        binding.productNameTextView.text = prod.name ?: "No name"
                        binding.productPriceTextView.text = "Price: $${prod.price}"
                        binding.productStocksTextView.text = "Stock: ${prod.stock}"
                        binding.productCategoryTextView.text = "Category: ${prod.category}"
                        binding.productVendorTextView.text = "Vendor: ${prod.vendorName}"
                    } ?: run {
                        Toast.makeText(this@ProductDetailActivity, "Product not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ProductDetailActivity", "Failed to load product details: $errorBody")
                    Toast.makeText(this@ProductDetailActivity, "Failed to load product details: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("ProductDetailActivity", "Error fetching product details: ${t.message}")
                Toast.makeText(this@ProductDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to add the product to the cart
    private fun addToCart() {
        // Ensure the product has been fetched
        product?.let { prod ->
            val cartItem = CartItem(
                id = generateCartItemId(), // Generate a unique ID for the cart item
                productId = prod.productId, // Use productId from the product object
                quantity = 1, // Default quantity
                userId = "currentUserId", // Replace with actual user ID
                name = prod.name ?: "No name", // Use name from the product object
                price = prod.price // Use price from the product object
            )

            // Log the cart item details before making the API call
            Log.d("ProductDetailActivity", "Adding to cart: $cartItem")

            // Call your API service to add the item to the cart
            apiService.addToCart(cartItem).enqueue(object : Callback<CartItem> {
                override fun onResponse(call: Call<CartItem>, response: Response<CartItem>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProductDetailActivity, "Added to cart", Toast.LENGTH_SHORT).show()
                        // Redirect to CartActivity after adding to cart
                        startActivity(Intent(this@ProductDetailActivity, CartActivity::class.java))
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("ProductDetailActivity", "Failed to add to cart: $errorBody")
                        Toast.makeText(this@ProductDetailActivity, "Failed to add to cart: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CartItem>, t: Throwable) {
                    Log.e("ProductDetailActivity", "Error adding to cart: ${t.message}")
                    Toast.makeText(this@ProductDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "Product details not available", Toast.LENGTH_SHORT).show()
        }
    }

    // Generate a unique ID for the cart item
    private fun generateCartItemId(): String {
        return "cartItem_${System.currentTimeMillis()}"
    }
}
