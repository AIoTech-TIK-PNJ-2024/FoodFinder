package com.aiotechpnj.foodfinder.utils

import android.content.Context
import com.aiotechpnj.foodfinder.data.PredictResult
import org.json.JSONArray
import java.io.IOException

class JsonLoader() {
    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
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

    fun parseJSONFromAsset(jsonString: String): List<PredictResult> {
        val predictItems = mutableListOf<PredictResult>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val item = PredictResult(
                id = jsonObject.getInt("id"),
                calories = jsonObject.getDouble("calories"),
                protein = jsonObject.getDouble("proteins"),
                fat = jsonObject.getDouble("fat"),
                carbohydrate = jsonObject.getDouble("carbohydrate"),
                name = jsonObject.getString("name"),
                image = jsonObject.getString("image")
            )
            predictItems.add(item)
        }
        return predictItems
    }
}