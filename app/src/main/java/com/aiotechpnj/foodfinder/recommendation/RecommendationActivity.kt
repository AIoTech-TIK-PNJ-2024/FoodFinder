package com.aiotechpnj.foodfinder.recommendation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.R
import com.aiotechpnj.foodfinder.data.InputData
import com.aiotechpnj.foodfinder.data.Item
import com.aiotechpnj.foodfinder.databinding.ActivityRecommendationBinding
import com.aiotechpnj.foodfinder.utils.identify_data
import com.aiotechpnj.foodfinder.utils.input_data
import com.bumptech.glide.Glide

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showInputData()
        showPredictionData()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showInputData(){
        val dataInput = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(input_data, InputData::class.java)
        } else {
            intent.getParcelableExtra(input_data)
        }

        binding.apply {
            val tempCal = getString(R.string.temp_cal)
            val tempG = getString(R.string.temp_g)

            tvCalories.text = String.format(tempCal, dataInput?.calories.toString())
            tvProtein.text = String.format(tempG, dataInput?.protein.toString())
            tvFat.text = String.format(tempG, dataInput?.fat.toString())
            tvCarbohydrate.text = String.format(tempG, dataInput?.carbohydrates.toString())
        }
    }

    private fun showPredictionData(){
        val predictionData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(identify_data, Item::class.java)
        } else {
            intent.getParcelableExtra(identify_data)
        }

        binding.apply {
            val tempCal = getString(R.string.temp_cal)
            val tempG = getString(R.string.temp_g)

            Glide.with(this@RecommendationActivity)
                .load(Uri.parse(predictionData?.image))
                .error(R.drawable.error_image)
                .into(ivFoodImage)

            tvFoodTitle.text = predictionData?.name
            predCalories.text = String.format(tempCal, predictionData?.calories.toString())
            predProtein.text = String.format(tempG, predictionData?.protein.toString())
            predCarbohydrate.text = String.format(tempG, predictionData?.carbohydrate.toString())
            predFat.text = String.format(tempG, predictionData?.fat.toString())
        }
    }
}