package com.bangkit.fracturevision.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.fracturevision.api.ApiConfig
import com.bangkit.fracturevision.model.RecordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class HomeApiStatus { LOADING, ERROR, DONE }
class HomeViewModel : ViewModel() {
    private val _datarecord = MutableLiveData<RecordResponse?>()
    val datarecord : LiveData<RecordResponse?> = _datarecord

    private val _status = MutableLiveData<HomeApiStatus>()
    val status : LiveData<HomeApiStatus> = _status

    fun getRecord(id: Int?) {
        _status.value = HomeApiStatus.LOADING
        val client = ApiConfig.getApiService().getRecord(id)
        client.enqueue(object : Callback<RecordResponse> {
            override fun onResponse(
                call: Call<RecordResponse>,
                response: Response<RecordResponse>
            ) {
                _status.value = HomeApiStatus.DONE
                if (response.isSuccessful) {
                    val resBody = response.body()
                    _datarecord.value = resBody
                }
            }
            override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
                _status.value = HomeApiStatus.ERROR
            }
        })

//        viewModelScope.launch {
//            try {
//                _status.value = HomeApiStatus.LOADING
//                val client = ApiConfig.getApiService().getRecord(id)
//                client.enqueue(object : Callback<RecordResponse> {
//                    override fun onResponse(
//                        call: Call<RecordResponse>,
//                        response: Response<RecordResponse>
//                    ) {
//                        _status.value = HomeApiStatus.DONE
//                        if (response.isSuccessful) {
//                            _datarecord.value = response.body()
//                        }
//                    }
//                    override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
//                        _status.value = HomeApiStatus.ERROR
//                    }
//                })
//            } catch (e: Exception) {
//                error(e.message.toString())
//            }
//        }
    }
}