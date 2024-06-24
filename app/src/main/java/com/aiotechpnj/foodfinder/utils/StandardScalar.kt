package com.aiotechpnj.foodfinder.utils

import android.annotation.SuppressLint

class StandardScalar {
    @SuppressLint("DefaultLocale")
    fun fitTransform(input: DoubleArray): DoubleArray {
        val mean = doubleArrayOf(203.21738484,  10.00118871,   7.58402675,  25.39019316)
        val scale = doubleArrayOf(163.01484065,  11.84357802,  13.7279605 ,  32.18109339)

        val inputScaled = input.mapIndexed { index, value ->
            val valueIndex = (value - mean[index]) / scale[index]
            String.format("%.8f", valueIndex).toDouble()
        }.toDoubleArray()

        return inputScaled
    }
}