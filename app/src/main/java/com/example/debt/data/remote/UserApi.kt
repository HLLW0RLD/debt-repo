package com.example.debt.data.remote

import com.example.debt.data.model.request.DebtRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    // user/settings -- через слеш для расширения и получения других данныхх юзера, например api/user/subscription


    @GET("api/user/settings")
    suspend fun getUserSettings(
        @Body request: DebtRequest
    ): Response<Unit>


    @POST("api/user/settings")
    suspend fun updateUserSettings(
        @Body request: DebtRequest
    ): Response<Unit>

}