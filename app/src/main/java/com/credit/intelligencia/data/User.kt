package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val idNo: String,
): Parcelable
