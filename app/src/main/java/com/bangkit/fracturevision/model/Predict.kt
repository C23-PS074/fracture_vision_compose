package com.bangkit.fracturevision.model

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

data class PredictResponse(
    val image_path: String,
    val prediction: String
)

data class RecordResponse(
    val datarecord: List<RecordItem>,
    val error: Boolean,
    val message: String
)

data class RecordItem(
    val date: String,
    val id: Int,
    val image: String,
    val result: String,
    val time: String
)