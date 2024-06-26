package com.aiotechpnj.foodfinder.recommendation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.R
import com.aiotechpnj.foodfinder.data.InputPredict
import com.aiotechpnj.foodfinder.data.PredictResult
import com.aiotechpnj.foodfinder.databinding.ActivityRecommendationBinding
import com.aiotechpnj.foodfinder.utils.input_data
import com.aiotechpnj.foodfinder.utils.predict_data
import com.bumptech.glide.Glide

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showInputData()
        showPredictionData()
//        binding.viewPager.adapter = SectionsPagerAdapter(this)
//        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()
//        supportActionBar?.elevation = 0f
    }

    private fun showInputData(){
        val dataInput = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(input_data, InputPredict::class.java)
        } else {
            intent.getParcelableExtra(input_data)
        }

        binding.apply {
            // Template String
            val tempCal = getString(R.string.temp_cal)
            val tempG = getString(R.string.temp_g)

            // Set Data
            tvCalories.text = String.format(tempCal, dataInput?.calories.toString())
            tvProtein.text = String.format(tempG, dataInput?.protein.toString())
            tvFat.text = String.format(tempG, dataInput?.fat.toString())
            tvCarbohydrate.text = String.format(tempG, dataInput?.carbohydrates.toString())
        }
    }

    private fun showPredictionData(){
        val predictionData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(input_data, PredictResult::class.java)
        } else {
            intent.getParcelableExtra(predict_data)
        }

        binding.apply {
            // Template String
            val tempCal = getString(R.string.temp_cal)
            val tempG = getString(R.string.temp_g)

            // Set Data
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_makanan,
            R.string.tab_minuman
        )
    }
}