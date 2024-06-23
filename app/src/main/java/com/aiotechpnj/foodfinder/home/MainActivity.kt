package com.aiotechpnj.foodfinder.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.databinding.ActivityMainBinding
import com.aiotechpnj.foodfinder.result.RecommendationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this, RecommendationActivity::class.java))
        }
    }
}