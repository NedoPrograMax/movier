package com.example.themovier.data

data class DataOrException<T, E:Exception?>(
    var data: T? = null,
  //  var loading: Boolean? = null,
    var e: E? = null
)
