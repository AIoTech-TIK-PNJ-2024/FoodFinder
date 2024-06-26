package com.aiotechpnj.foodfinder.utils

import org.json.JSONArray

class LabelEncoder {
    private val labelToIndex = mutableMapOf<String, Int>()
    private val indexToLabel = mutableMapOf<Int, String>()

    fun fit(labels: String) {
        val jsonArray = JSONArray(labels)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val name = jsonObject.getString("name")
            labelToIndex[name] = id
            indexToLabel[id] = name
        }
    }

    fun inverseTransform(index: Int): String {
        return indexToLabel[index] ?: error("Index $index not found in encoder.")
    }

    fun classes(): List<String> {
        return labelToIndex.keys.toList()
    }
}