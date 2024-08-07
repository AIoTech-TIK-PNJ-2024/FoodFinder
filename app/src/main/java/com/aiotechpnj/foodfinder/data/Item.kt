package com.aiotechpnj.foodfinder.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item (
    val id: Int,
    val category: String,
    val name: String,
    val image: String,
    val calories: Double? = 0.0,
    val protein: Double? = 0.0,
    val fat: Double? = 0.0,
    val carbohydrate: Double? = 0.0
): Parcelable