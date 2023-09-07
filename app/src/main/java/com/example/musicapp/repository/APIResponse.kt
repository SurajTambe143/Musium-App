package com.example.musicapp.repository

sealed class APIResponse<T>(val data: T? = null, val errorMessage: String? = null) {

    class Loading<T>() : APIResponse<T>()
    class Success<T>(data: T? = null) : APIResponse<T>(data = data)
    class Error<T>(errorMessage: String) : APIResponse<T>(errorMessage = errorMessage)
}