package com.bangkit.fracturevision.api

import com.bangkit.fracturevision.model.LoginResponse
import com.bangkit.fracturevision.model.PredictResponse
import com.bangkit.fracturevision.model.RecordResponse
import com.bangkit.fracturevision.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("fullname") fullname: String,
        @Field("phone") phone: String,
        @Field("address") address: String
    ) : Call<RegisterResponse>

    @Multipart
    @POST("predict")
    fun predict(
        @Part("id") id: Int,
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("record")
    fun getRecord(
        @Query("id") id: Int? = null
    ) : Call<RecordResponse>
}