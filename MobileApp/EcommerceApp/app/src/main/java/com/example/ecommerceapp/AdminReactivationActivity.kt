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

class AdminReactivationActivity : AppCompatActivity() {

    private lateinit var reactivationList: ListView
    private lateinit var accounts: MutableList<String>
    private lateinit var retrofit: Retrofit
    private lateinit var ecommerceService: EcommerceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_reactivation)

        reactivationList = findViewById(R.id.reactivationList)
        accounts = mutableListOf()

        // Initialize Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7047/api/Accounts/reactivate")  // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ecommerceService = retrofit.create(EcommerceService::class.java)

        // TODO: Fetch deactivated accounts from C# web service (mocked for now)
        accounts.add("deactivatedUser1@example.com")
        accounts.add("deactivatedUser2@example.com")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, accounts)
        reactivationList.adapter = adapter

        // Handle clicking on an item to reactivate the account
        reactivationList.setOnItemClickListener { _, _, position, _ ->
            val email = accounts[position]
            reactivateAccount(email)
        }
    }

    private fun reactivateAccount(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ecommerceService.reactivateAccount(email)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminReactivationActivity, "$email reactivated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdminReactivationActivity, "Failed to reactivate $email", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminReactivationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
