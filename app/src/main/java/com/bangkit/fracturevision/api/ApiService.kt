package com.bangkit.fracturevision.api

import com.bangkit.fracturevision.model.LoginResponse
import com.bangkit.fracturevision.model.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @Multipart
    @POST("predict")
    fun predict(
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>
}