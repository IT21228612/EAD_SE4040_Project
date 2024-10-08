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

class FeedbackActivity : AppCompatActivity() {

    private lateinit var vendorNameInput: EditText
    private lateinit var rankInput: EditText
    private lateinit var commentInput: EditText
    private lateinit var submitButton: Button
    private lateinit var ecommerceService: EcommerceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)  // Replace with your layout file name

        vendorNameInput = findViewById(R.id.vendorName)
        rankInput = findViewById(R.id.rank)
        commentInput = findViewById(R.id.comment)
        submitButton = findViewById(R.id.submitButton)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7047/api/Feedback")  // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ecommerceService = retrofit.create(EcommerceService::class.java)

        submitButton.setOnClickListener {
            submitFeedback()
        }
    }

    private fun submitFeedback() {
        val vendorName = vendorNameInput.text.toString()
        val rank = rankInput.text.toString()
        val comment = commentInput.text.toString()

        // Validate input
        if (vendorName.isEmpty() || rank.isEmpty() || comment.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val feedback = Feedback(vendorName, rank, comment)

        // Coroutine for network call
        CoroutineScope(Dispatchers.IO).launch {
            val response = ecommerceService.submitFeedback(feedback)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FeedbackActivity, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FeedbackActivity, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
