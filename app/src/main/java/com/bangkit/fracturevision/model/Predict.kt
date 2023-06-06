package com.bangkit.fracturevision.model

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

data class PredictResponse(
    val prediction: String
)

abstract class JsonNavType<T> : NavType<T>(isNullableAllowed = false) {
    abstract fun fromJsonParse(value: String): T
    abstract fun T.getJsonParse(): String

    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let { parseValue(it) }

    override fun parseValue(value: String): T = fromJsonParse(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, value.getJsonParse())
    }
}

class ResultArgType : JsonNavType<PredictResponse>() {
    override fun fromJsonParse(value: String): PredictResponse {
        return Gson().fromJson(value, PredictResponse::class.java)
    }

    override fun PredictResponse.getJsonParse(): String {
        return Gson().toJson(this@ResultArgType)
    }
}
