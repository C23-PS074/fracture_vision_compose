package com.bangkit.fracturevision.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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

class LoginViewModel(private val appViewModel: AppViewModel) : ViewModel(){

    fun loginApi(username: String, password: String, context: Context, navigateToHome : () -> Unit) {
        val client = ApiConfig.getApiService().login(username, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        val data = responseBody.data[0]
                        val user = User(data.id, data.username, true)
                        appViewModel.login(user)
                        navigateToHome()
                        Toast.makeText(context, "Welcome ${data.nama}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Username or Password Wrong", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Failed To Login", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class ViewModelFactory(private val appViewModel: AppViewModel) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(appViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}