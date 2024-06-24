package com.aiotechpnj.foodfinder.utils

class LabelEncoder {
    private val labelToIndex = mutableMapOf<String, Int>()
    private val indexToLabel = mutableMapOf<Int, String>()

    fun fit(labels: List<String>) {
        labels.distinct().forEachIndexed { index, label ->
            labelToIndex[label] = index
            indexToLabel[index] = label
        }
    }

    fun inverseTransform(index: Int): String {
        return indexToLabel[index] ?: error("Index $index not found in encoder.")
    }

    fun classes(): List<String> {
        return labelToIndex.keys.toList()
    }
}