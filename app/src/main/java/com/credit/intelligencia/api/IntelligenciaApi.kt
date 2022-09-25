package com.credit.intelligencia.api

import android.icu.text.IDNA
import android.widget.EditText
import com.credit.intelligencia.data.LimitScore
import com.credit.intelligencia.data.Message
import com.credit.intelligencia.data.ScoreLimit
import com.squareup.moshi.Json
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface IntelligenciaApi {

    @POST("signup")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("firstname")  firstname: String?,
        @Field ("lastname") lastname: String,
        @Field ("email") email: String,
        @Field ("password") password: String,
        @Field("phone") phone: String,
        @Field ("idnum") idnum: String
    ): LoginResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field ("email") email: String,
        @Field ("password") password: String
    ): LoginResponse

    @POST("scorelimit")
    @FormUrlEncoded
    suspend fun getScoreLimit(
        @Field ("email") email: String
    ): Response<Any>

    @POST("store")
    @Multipart
    suspend fun postFiles(
        @Part("info") info: Infor,
        @Part application:MultipartBody.Part,
        @Part contacts: MultipartBody.Part,
        @Part calllogs: MultipartBody.Part,
        @Part message: MultipartBody.Part
    ):ApiResponse<Any>
}
data class Infor(val email: String)