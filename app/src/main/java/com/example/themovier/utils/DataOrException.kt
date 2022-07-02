package com.example.themovier.utils

data class DataOrException<T, E : Exception?>(
    var data: T? = null,
    //  var loading: Boolean? = null,
    var e: E? = null
) {
    val a: Result<Int> = Result.success(1)

    init {
    }
}
