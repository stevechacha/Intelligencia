package com.credit.intelligencia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventObject {

    val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>> get() = statusMessage
}