package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class IntroSlide(
    val title: String,
    val description: String,
    val icon: String
):Parcelable
