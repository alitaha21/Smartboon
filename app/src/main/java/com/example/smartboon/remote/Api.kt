package com.example.smartboon.remote

import com.example.smartboon.model.APIResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("order.php")
    fun order(
        @Field("open") open: Int
    ): Call<APIResponse>

}