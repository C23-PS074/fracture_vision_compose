package com.bangkit.fracturevision.screen.register

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.fracturevision.api.ApiConfig
import com.bangkit.fracturevision.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class RegisterApiStatus { LOADING, ERROR, DONE }

class RegisterViewModel : ViewModel() {
    private val _status = MutableLiveData<RegisterApiStatus>()
    val status : LiveData<RegisterApiStatus> = _status
    fun registerApi(
        username: String,
        password: String,
        fullname: String,
        phone: String,
        address: String,
        context: Context,
        navigateToLogin : () -> Unit
    ) {
        _status.value = RegisterApiStatus.LOADING
        val client = ApiConfig.getApiService().register(
            username, password, fullname, phone, address
        )
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _status.value = RegisterApiStatus.DONE
                if (response.isSuccessful) {
                    val resBody = response.body()
                    if (resBody != null) {
                        Toast.makeText(context, resBody.message, Toast.LENGTH_SHORT).show()
                        if (!resBody.error) {
                            navigateToLogin()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _status.value = RegisterApiStatus.ERROR
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}