package com.example.omdb.data

sealed class ResponseType<out T>(val status: Status, val _data: T?, val exception: Throwable?) {

    data class Success<out R>(val data: R) : ResponseType<R>(
        status = Status.SUCCESS,
        _data = data,
        exception = null
    )

    data class Loading(val isLoading: Boolean) : ResponseType<Nothing>(
        status = Status.LOADING,
        _data = null,
        exception = null
    )

    data class Error(val ex: Throwable) : ResponseType<Nothing>(
        status = Status.ERROR,
        _data = null,
        exception = ex
    )
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}
