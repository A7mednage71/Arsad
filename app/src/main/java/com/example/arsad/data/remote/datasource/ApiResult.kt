package com.example.arsad.data.remote.datasource

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}
