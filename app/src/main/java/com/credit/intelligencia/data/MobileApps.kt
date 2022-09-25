package com.credit.intelligencia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class MobileApps(
    val appName: String
): Parcelable
