package com.bangkit.fracturevision.model

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