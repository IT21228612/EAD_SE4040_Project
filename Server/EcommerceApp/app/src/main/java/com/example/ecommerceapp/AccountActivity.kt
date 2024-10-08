package com.example.ecommerceapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccountActivity : AppCompatActivity() {
    private lateinit var accountStatusText: TextView
    private lateinit var deactivateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        accountStatusText = findViewById(R.id.Status)
        deactivateButton = findViewById(R.id.deactivate)

        deactivateButton.setOnClickListener {
            deactivateAccount("user@example.com") // Replace with actual email
        }
    }

    private fun deactivateAccount(email: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7047/api/Accounts/deactivate")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EcommerceService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.reactivateAccount(email) // Assuming reactivateAccount handles both deactivate/reactivate
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    accountStatusText.text = "Account Deactivated"
                    Toast.makeText(this@AccountActivity, "Account deactivated", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@AccountActivity, "Failed to deactivate", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
