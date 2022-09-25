package com.credit.intelligencia.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LimitScore(
    val score:Int,
    val limit: Int
)
