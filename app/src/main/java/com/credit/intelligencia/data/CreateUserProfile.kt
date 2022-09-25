package com.credit.intelligencia.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class CreateUserProfile(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String,
    val idnum: String
):Parcelable