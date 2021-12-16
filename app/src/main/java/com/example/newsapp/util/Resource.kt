package com.example.newsapp.util

import android.security.identity.InvalidRequestMessageException

sealed class Resource<T>(var data: T?=  null,
var message: String? = null){

    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? =null ,message: String) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

}