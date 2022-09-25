package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val address: String?,
    val body: String,
    val date: String,
    val time: String,
    val id: String,
): Parcelable