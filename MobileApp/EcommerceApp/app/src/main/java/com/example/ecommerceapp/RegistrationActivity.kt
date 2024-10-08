package com.example.ecommerceapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createAccount(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // Initialize Retrofit to send request to the C# web service
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7047/api/Accounts")  // Replace with your server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EcommerceService::class.java)
        val accountRequest = AccountRequest(email, password)

        // Use Coroutine to handle network requests on background thread
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.createAccount(accountRequest)
            if (response.isSuccessful) {
                // Switch back to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistrationActivity, "Account created. Awaiting approval.", Toast.LENGTH_LONG).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistrationActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
