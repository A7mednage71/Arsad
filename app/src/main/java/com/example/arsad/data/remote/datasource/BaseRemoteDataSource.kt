package com.example.arsad.data.remote.datasource

abstract class BaseRemoteDataSource {
    // One function that models Success and Failure for all requests
    suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Failure("Server error: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Failure(e.localizedMessage ?: "An unexpected error occurred...!")
        }
    }
}