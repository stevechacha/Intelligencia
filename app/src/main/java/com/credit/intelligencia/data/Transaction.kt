package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class Transaction(
    val amount: Int,
    val totalAmount: Double,
    val date: String
): Parcelable
