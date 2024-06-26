package com.aiotechpnj.foodfinder.utils

import android.content.Context
import com.aiotechpnj.foodfinder.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showMessage(context: Context, message: String){
    MaterialAlertDialogBuilder(context)
        .setTitle(context.getString(R.string.error))
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.oke)){ dialog, _ ->
            dialog.dismiss()
        }
        .show()
}