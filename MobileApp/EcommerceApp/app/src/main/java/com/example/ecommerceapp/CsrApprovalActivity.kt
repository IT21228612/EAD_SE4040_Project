package com.example.ecommerceapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CsrApprovalActivity : AppCompatActivity() {

    private lateinit var pendingAccountsList: ListView
    private lateinit var accounts: MutableList<String>
    private lateinit var retrofit: Retrofit
    private lateinit var ecommerceService: EcommerceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_csr_approval)

        pendingAccountsList = findViewById(R.id.pendingAccountsList)
        accounts = mutableListOf()

        // Initialize Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7047/api/Accounts/approve")  // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ecommerceService = retrofit.create(EcommerceService::class.java)

        // TODO: Fetch pending accounts from C# web service (mocked for now)
        accounts.add("user1@example.com")
        accounts.add("user2@example.com")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, accounts)
        pendingAccountsList.adapter = adapter

        // Handle clicking on an item to approve the account
        pendingAccountsList.setOnItemClickListener { _, _, position, _ ->
            val email = accounts[position]

            // Here you can choose to either approve or reject the account
            approveOrRejectAccount(email, true) // true for approve, false for reject
        }
    }

    private fun approveOrRejectAccount(email: String, isApprove: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (isApprove) {
                    ecommerceService.approveAccount(email)
                } else {
                    ecommerceService.rejectAccount(email)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val action = if (isApprove) "approved" else "rejected"
                        Toast.makeText(this@CsrApprovalActivity, "$email $action", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CsrApprovalActivity, "Failed to process $email", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CsrApprovalActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
