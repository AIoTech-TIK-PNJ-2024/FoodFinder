package com.aiotechpnj.foodfinder.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputData (
    val calories: Float? = 0f,
    val protein: Float? = 0f,
    val fat: Float? = 0f,
    val carbohydrates: Float? = 0f
) : Parcelable