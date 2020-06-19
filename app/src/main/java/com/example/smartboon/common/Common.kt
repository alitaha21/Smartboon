package com.example.smartboon.common

import com.example.smartboon.remote.Api
import com.example.smartboon.remote.RetrofitClient

object Common {

    private const val BASE_URL = "http://10.0.2.2/mysql/Smartboon/"

    val api: Api
        get() = RetrofitClient.getClient(BASE_URL).create(Api::class.java)

}