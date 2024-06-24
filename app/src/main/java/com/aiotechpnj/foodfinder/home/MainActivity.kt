package com.aiotechpnj.foodfinder.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.databinding.ActivityMainBinding
import com.aiotechpnj.foodfinder.utils.LabelEncoder
import com.aiotechpnj.foodfinder.utils.StandardScalar
import org.json.JSONArray
import org.tensorflow.lite.Interpreter
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        predict()
    }

    private fun predict(){
        // val model = FoodPredictionModel.newInstance(this)
        val interpreter = Interpreter(loadModelFile())

        binding.apply {
            btnSubmit.setOnClickListener {
                val calories = edCalories.text.toString().toDouble()
                val carbohydrates = edCarbohydrate.text.toString().toDouble()
                val protein = edProtein.text.toString().toDouble()
                val fat = edFat.text.toString().toDouble()

                // Normalize Input
                val input = doubleArrayOf(calories, protein, fat, carbohydrates)
                val scalar = StandardScalar()
                val inputScaled = scalar.fitTransform(input)

                // LabelEncoder
                val jsonString = loadJSONFromAsset()
                val foodData = parseLabelsFromJSON(jsonString ?: "")
                val labels = foodData.map { it }
                val labelEncoder = LabelEncoder()
                labelEncoder.fit(labels)

                val byteBuffer = ByteBuffer.allocateDirect(4 * inputScaled.size).order(ByteOrder.nativeOrder())
                inputScaled.forEach { byteBuffer.putFloat(it.toFloat()) }

                // Perform prediction
                val output = Array(1) { FloatArray(labelEncoder.classes().size) }
                interpreter.run(byteBuffer, output)

                // Get predicted label
                val predictedIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
                val predictedLabel = labelEncoder.inverseTransform(predictedIndex)

                // Display result
                Log.d("Input", "${inputScaled[0].toFloat()} ${inputScaled[1]} ${inputScaled[2]} ${inputScaled[3]}")
                Log.d("Output", "Predicted food: $predictedLabel")
            }
        }
    }

    private fun loadJSONFromAsset(): String? {
        return try {
            val inputStream = assets.open("nutrition.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = assets.openFd("food_prediction_model.tflite")
        val fileInputStream = assetFileDescriptor.createInputStream()
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun parseLabelsFromJSON(jsonString: String): List<String> {
        val labels = mutableListOf<String>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            labels.add(name)
        }
        return labels
    }
}