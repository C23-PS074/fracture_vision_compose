package com.bangkit.fracturevision

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.fracturevision.api.ApiConfig
import com.bangkit.fracturevision.model.PredictResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

enum class ScanApiStatus { LOADING, DONE, ERROR }

class PredictViewModel : ViewModel() {
    private val _data = MutableLiveData<PredictResponse?>()
    val data : LiveData<PredictResponse?> = _data

    private val _status = MutableLiveData<ScanApiStatus>()
    val status : LiveData<ScanApiStatus> = _status

    fun uploadImage(id: Int, file: File, context: Context, navigateToResult: () -> Unit) {
        _status.value = ScanApiStatus.LOADING
        val imageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            imageFile
        )
        val client = ApiConfig.getApiService().predict(
            id,
            imageMultipart
        )
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _status.value = ScanApiStatus.DONE
                        _data.value = responseBody
                        navigateToResult()
                    } else {
                        _status.value = ScanApiStatus.DONE
                        Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _status.value = ScanApiStatus.ERROR
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}