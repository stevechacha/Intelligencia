package com.credit.intelligencia.util

sealed class Environment(val url: String) {
    object Main : Environment("https://api.com/noncustomer/")
}
