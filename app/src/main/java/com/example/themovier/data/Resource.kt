package com.example.themovier.data

sealed class Resource<T>(val data: T? = null) {
    class OnSuccess<T>(data: T) : Resource<T>(data)
    class OnError<T>(data: T? = null) : Resource<T>(data)
}

