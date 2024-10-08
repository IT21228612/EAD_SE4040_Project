package com.example.ecommerceapp.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.adapters.CartAdapter
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityCartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var cartAdapter: CartAdapter
    private var cartItems: MutableList<CartItem> = mutableListOf()
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()

        // Set up RecyclerView for cart items
        cartAdapter = CartAdapter(cartItems, onCartItemUpdated, onCartItemRemoved)
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = cartAdapter

        // Fetch cart items
        fetchCartItems()

        // Handle the checkout button click
        binding.checkoutButton.setOnClickListener {
            showCheckoutConfirmationDialog()
        }
    }

    private fun fetchCartItems() {
        apiService.getCartItems().enqueue(object : Callback<List<CartItem>> {
            override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                if (response.isSuccessful) {
                    cartItems.clear()
                    cartItems.addAll(response.body() ?: emptyList())
                    cartAdapter.notifyDataSetChanged()
                    updateTotalPrice() // Update the total price after fetching items
                } else {
                    Toast.makeText(this@CartActivity, "Failed to fetch cart items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val onCartItemUpdated: (CartItem) -> Unit = { cartItem ->
        // Update cart item logic goes here
    }

    private val onCartItemRemoved: (CartItem) -> Unit = { cartItem ->
        // Remove cart item logic goes here
    }

    private fun showCheckoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Checkout")
            .setMessage("Are you sure you want to checkout?")
            .setPositiveButton("Yes") { _, _ -> createOrder() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun createOrder() {
        // Create a list to hold the products
        val products = mutableListOf<Product>()

        // Create a list of fetch tasks for each cart item
        val fetchTasks = cartItems.map { cartItem ->
            fetchProductById(cartItem.productId) { product ->
                product?.let {
                    products.add(it) // Add the fetched product to the list
                }

                // Check if all products have been fetched
                if (products.size == cartItems.size) {
                    // Create the order after all products have been fetched
                    val order = Order(
                        OrderId = generateOrderId(),
                        customerId = "currentUserId", // Replace with actual user ID
                        products = products, // Use the full list of Product objects
                        isPurchased = false,
                        status = "Pending",
                        createdAt = System.currentTimeMillis().toString()
                    )
                    placeOrder(order)
                }
            }
        }

        // Execute all fetch tasks
    }

    private fun fetchProductById(productId: String, callback: (Product?) -> Unit) {
        apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Toast.makeText(this@CartActivity, "Failed to fetch product details", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }

    private fun placeOrder(order: Order) {
        apiService.createOrder(order).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CartActivity, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close activity after successful order
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(this@CartActivity, "Failed to place order: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price * it.quantity } // Calculate total price
        binding.totalPriceText.text = "Total: $$totalPrice" // Display total price
    }

    private fun generateOrderId(): String {
        return "order_${System.currentTimeMillis()}" // Generate a unique order ID
    }
}
