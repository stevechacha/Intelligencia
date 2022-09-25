package com.credit.intelligencia.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class ScoreLimit(
    @Json(name = "score")
    val score:Int,
    @Json(name="limit")
    val limit: Int
):Parcelable
