package com.bangkit.fracturevision.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.api.ApiConfig
import com.bangkit.fracturevision.model.LoginResponse
import com.bangkit.fracturevision.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class LoginApiStatus { LOADING, ERROR, DONE }
class LoginViewModel(private val appViewModel: AppViewModel) : ViewModel(){
    private val _status = MutableLiveData<LoginApiStatus>()
    val status : LiveData<LoginApiStatus> = _status

    fun loginApi(username: String, password: String, context: Context, navigateToHome : () -> Unit) {
        _status.value = LoginApiStatus.LOADING
        val client = ApiConfig.getApiService().login(username, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        _status.value = LoginApiStatus.DONE
                        val data = responseBody.data[0]
                        val user = User(data.id, data.username, true)
                        appViewModel.login(user)
                        navigateToHome()
                        Toast.makeText(context, "Welcome ${data.nama}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    _status.value = LoginApiStatus.DONE
                    Toast.makeText(context, "Username or Password Wrong", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _status.value = LoginApiStatus.ERROR
                Toast.makeText(context, "Failed To Login", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class LoginViewModelFactory(private val appViewModel: AppViewModel) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(appViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}