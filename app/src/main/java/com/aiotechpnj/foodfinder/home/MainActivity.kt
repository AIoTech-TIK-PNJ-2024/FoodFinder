package com.aiotechpnj.foodfinder.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.data.InputPredict
import com.aiotechpnj.foodfinder.data.PredictResult
import com.aiotechpnj.foodfinder.databinding.ActivityMainBinding
import com.aiotechpnj.foodfinder.recommendation.RecommendationActivity
import com.aiotechpnj.foodfinder.utils.JsonLoader
import com.aiotechpnj.foodfinder.utils.LabelEncoder
import com.aiotechpnj.foodfinder.utils.StandardScalar
import com.aiotechpnj.foodfinder.utils.input_data
import com.aiotechpnj.foodfinder.utils.predict_data
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var interpreter: Interpreter? = null
    private var inputData: InputPredict? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSubmit.setOnClickListener {
                val calories = edCalories.text.toString().toDouble()
                val carbohydrates = edCarbohydrate.text.toString().toDouble()
                val protein = edProtein.text.toString().toDouble()
                val fat = edFat.text.toString().toDouble()
                predict(calories, protein, fat, carbohydrates)
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
            .loadJSONFromAsset(this@MainActivity, "nutrition_encoded.json")
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

        Log.d("predictedLabel", predictedLabel)
        val jsonNutrition = JsonLoader().loadJSONFromAsset(this@MainActivity, "nutrition.json")
        val data = JsonLoader().parseJSONFromAsset(jsonNutrition ?: "")
        val predictItems = findFoodItemByName(data, predictedLabel)
        Log.d("predictItems", predictItems.toString())

        if (predictItems != null) {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra(predict_data, predictItems)
            val inputData = InputPredict(
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

    private fun findFoodItemByName(predictItems: List<PredictResult>, name: String): PredictResult? {
        return predictItems.find { it.name == name }
    }
}