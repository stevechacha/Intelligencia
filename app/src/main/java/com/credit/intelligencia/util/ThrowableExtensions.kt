package com.credit.intelligencia.util

import androidx.annotation.Keep
import com.credit.intelligencia.api.ApiException
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException
import timber.log.Timber

fun Throwable.handleThrowable(): String {
    Timber.e(this)
    val message = when {
        this is ApiException -> this.statusMessage
        this is UnknownHostException -> "We've detected a network problem. Please check your internet connection and try again"
        this is HttpException && this.code() == 403 -> "Not authorized"
        this is HttpException && this.code() in 500..599 -> "Sorry, we are currently unable to complete your request. Please try again later"
        this is HttpException -> convertHttpApiError(this)
            ?: "Sorry, we are currently unable to complete your request. Please try again later"
        this is SocketTimeoutException -> "Please check your connectivity and try again"
        else -> this.message
            ?: "Sorry, we are currently unable to complete your request. Please try again later"
    }
    return message
}

fun convertHttpApiError(exception: HttpException): String? {
    val adapter: JsonAdapter<ApiError> =
        Moshi.Builder().build().adapter(ApiError::class.java).lenient()
    exception.response()?.run {
        errorBody()?.let { responseBody ->
            val errorJson = responseBody.string()
            if (!errorJson.contains("{"))
                return errorJson
            else {
                val apiError = adapter.fromJson(errorJson)
                apiError?.let { error ->
                    error.errorDescription?.let {
                        return it
                    }
                    error.statusMessage?.let {
                        return it
                    }
                }
            }
        }
    }
    return null
}

@Keep
@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "error")
    val error: String?,
    @Json(name = "error_code")
    val errorCode: String = "",
    @Json(name = "error_description")
    val errorDescription: String?,
    @Json(name = "statusCode")
    val statusCode: String?,
    @Json(name = "statusMessage")
    val statusMessage: String?
)