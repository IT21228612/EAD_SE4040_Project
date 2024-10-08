package com.example.ecommerceapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.browseProductsButton.setOnClickListener {
            startActivity(Intent(this,ProductActivity::class.java))
        }

        binding.manageCartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.orderTrackingButton.setOnClickListener {
            startActivity(Intent(this, OrderTrackingActivity::class.java))
        }
    }
}
