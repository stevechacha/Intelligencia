package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contacts(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val number: String,
): Parcelable
