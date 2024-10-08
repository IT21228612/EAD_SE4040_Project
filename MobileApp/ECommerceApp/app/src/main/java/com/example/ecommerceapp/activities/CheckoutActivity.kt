package com.example.ecommerceapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityCheckoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()

        // Retrieve cart items passed from the previous activity
        val cartItems = intent.getParcelableArrayListExtra<CartItem>("cartItems") ?: arrayListOf()

        // If there are no cart items, disable checkout
        if (cartItems.isEmpty()) {
            showToast("Your cart is empty!")
            return
        }

        binding.checkoutButton.setOnClickListener {
            // Create order with the cart items
            createOrder(cartItems)
        }
    }

    private fun createOrder(cartItems: List<CartItem>) {
        // Create a list to hold the products
        val products = mutableListOf<Product>()

        // Create a list to hold all fetch tasks
        val fetchTasks = cartItems.map { cartItem ->
            fetchProductById(cartItem.productId) { product ->
                product?.let {
                    products.add(
                        Product(
                            productId = it.productId, // Ensure this matches your Product model
                            name = it.name,
                            price = it.price,
                            stock = it.stock,
                            category = it.category,
                            isActive = it.isActive,
                            vendorId = it.vendorId,
                            vendorName = it.vendorName // Adjust according to your Product model
                        )
                    )

                    // Check if all products have been fetched
                    if (products.size == cartItems.size) {
                        // Create the order after all products have been fetched
                        placeOrder(products)
                    }
                }
            }
        }
    }

    // Function to fetch product by ID
    private fun fetchProductById(productId: String, callback: (Product?) -> Unit) {
        apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    showToast("Failed to fetch product details")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                showToast("Error: ${t.message}")
                callback(null)
            }
        })
    }

    // Function to place the order
    private fun placeOrder(products: List<Product>) {
        // Create the order using the Product objects
        val order = Order(
            OrderId = generateOrderId(),
            customerId = generateCustomerId(),  // Replace with actual customer ID from user session
            products = products,                // Use the list of Product objects
            isPurchased = false,                // Default value for isPurchased
            status = "Pending",                 // Status set as "Pending"
            createdAt = System.currentTimeMillis().toString() // Adjust to your desired date format
        )

        // Make the API call to create the order
        apiService.createOrder(order).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    showToast("Order placed successfully!")
                    finish() // Close activity after successful order
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    showToast("Failed to place order: $errorBody")
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Helper function to generate a unique order ID
    private fun generateOrderId(): String {
        return "order_${System.currentTimeMillis()}"
    }

    // This should ideally come from user session or login context
    private fun generateCustomerId(): String {
        return "customer_${System.currentTimeMillis()}" // Placeholder customer ID
    }

    // Helper function to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
