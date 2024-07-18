package com.aiotechpnj.foodfinder.recommendation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.R
import com.aiotechpnj.foodfinder.data.InputData
import com.aiotechpnj.foodfinder.data.Item
import com.aiotechpnj.foodfinder.databinding.ActivityNutritionBinding
import com.aiotechpnj.foodfinder.utils.JsonLoader
import com.aiotechpnj.foodfinder.utils.LabelEncoder
import com.aiotechpnj.foodfinder.utils.StandardScalar
import com.aiotechpnj.foodfinder.utils.identify_data
import com.aiotechpnj.foodfinder.utils.input_data
import com.aiotechpnj.foodfinder.utils.showMessage
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer

class NutritionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNutritionBinding
    private var interpreter: Interpreter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSubmit.setOnClickListener {
                val calories = edCalories.text.toString()
                val carbohydrates = edCarbohydrate.text.toString()
                val protein = edProtein.text.toString()
                val fat = edFat.text.toString()
                if (calories.isNotEmpty() && carbohydrates.isNotEmpty() &&
                    protein.isNotEmpty() && fat.isNotEmpty())
                {
                    predict(
                        calories.toDouble(),
                        protein.toDouble(),
                        fat.toDouble(),
                        carbohydrates.toDouble()
                    )
                } else {
                    showMessage(this@NutritionActivity, getString(R.string.empty_data))
                }
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun predict(calories: Double, protein: Double, fat: Double, carbohydrates: Double){
        // Define Model
        val interpreter = getInterpreter()

        // Normalize Input
        val input = doubleArrayOf(calories, protein, fat, carbohydrates)
        val scalar = StandardScalar()
        val inputScaled = scalar.fitTransform(input)

        // LabelEncoder
        val jsonNutritionEncoded = JsonLoader()
            .loadJSONFromAsset(this@NutritionActivity, "nutrition_encoded.json")
        val labelEncoder = LabelEncoder()
        labelEncoder.fit(jsonNutritionEncoded ?: "")

        // Perform prediction
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputScaled.size).order(ByteOrder.nativeOrder())
        inputScaled.forEach { byteBuffer.putFloat(it.toFloat()) }
        val output = Array(1) { FloatArray(labelEncoder.classes().size) }
        interpreter.run(byteBuffer, output)

        // Get predicted label
        val predictedIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val predictedLabel = labelEncoder.inverseTransform(predictedIndex)

        if (predictedIndex == -1) {
            showMessage(this@NutritionActivity, getString(R.string.failed_predict))
        } else {
            Log.d("predictedLabel", predictedLabel)
            val jsonNutrition = JsonLoader().loadJSONFromAsset(this@NutritionActivity, "nutrition.json")
            val data = JsonLoader().parseJSONFromAsset(jsonNutrition ?: "")
            val predictItems = findFoodItemByName(data, predictedLabel)
            Log.d("predictItems", predictItems.toString())

            if (predictItems != null) {
                val intent = Intent(this, RecommendationActivity::class.java)
                intent.putExtra(identify_data, predictItems)
                val inputData = InputData(
                    input[0].toFloat(),
                    input[1].toFloat(),
                    input[2].toFloat(),
                    input[3].toFloat()
                )
                Log.d("inputData", inputData.toString())
                intent.putExtra(input_data, inputData)
                startActivity(intent)
            }
        }
    }

    private fun getInterpreter(): Interpreter {
        if (interpreter == null) {
            val options = Interpreter.Options()
            interpreter = Interpreter(loadModelFile(this), options)
        }
        return interpreter!!
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        return FileUtil.loadMappedFile(context, "food_prediction_model.tflite")
    }

    private fun findFoodItemByName(predictItems: List<Item>, name: String): Item? {
        return predictItems.find { it.name == name }
    }
}