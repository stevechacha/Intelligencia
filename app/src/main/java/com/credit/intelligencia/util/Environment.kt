package com.credit.intelligencia.util

sealed class Environment(val url: String) {
    object Main : Environment("https://api-uat.equitygroupholdings.com/noncustomer/")
}