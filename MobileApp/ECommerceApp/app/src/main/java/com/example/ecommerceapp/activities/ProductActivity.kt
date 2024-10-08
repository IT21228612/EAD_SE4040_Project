package com.example.ecommerceapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.network.ApiService
import com.example.ecommerceapp.databinding.ActivityProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityProductBinding
    private var allProducts: List<Product> = listOf() // Store all products for filtering
    private var filteredProducts: List<Product> = listOf() // Store filtered products for display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()
        productAdapter = ProductAdapter(listOf()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("productId", product.productId) // Ensure ProductId is being set correctly
            startActivity(intent)
        }



        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = productAdapter

        // Fetch products
        fetchProducts()

        // Set up search functionality
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString()) // Filter products based on the search bar input
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set up filter button
        binding.filterButton.setOnClickListener {
            showCategoryFilterDialog()
        }
    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    allProducts = response.body() ?: emptyList() // Store the full product list
                    filteredProducts = allProducts // Initialize filtered products with all products
                    productAdapter.updateProducts(filteredProducts) // Update the adapter with all products
                } else {
                    Toast.makeText(this@ProductActivity, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterProducts(query: String) {
        // Filter based on the search query
        val filteredBySearch = allProducts.filter { product ->
            product.name.contains(query, ignoreCase = true) // Adjust this to your product name field
        }
        productAdapter.updateProducts(filteredBySearch) // Update the adapter with filtered products
    }

    private fun showCategoryFilterDialog() {
        val categories = arrayOf("All Products", "Electronics", "Clothing", "Home & Kitchen") // Example categories
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a Category")
            .setItems(categories) { dialog, which ->
                val selectedCategory = categories[which]
                filterByCategory(selectedCategory) // Call the function to filter by the selected category
            }
            .show()
    }

    private fun filterByCategory(category: String) {
        // Filter based on the selected category
        filteredProducts = if (category == "All Products") {
            allProducts // Show all products if "All Products" is selected
        } else {
            allProducts.filter { product ->
                product.category.equals(category, ignoreCase = true) // Adjust this to match your category field
            }
        }
        productAdapter.updateProducts(filteredProducts) // Update the adapter with filtered products
    }
}
