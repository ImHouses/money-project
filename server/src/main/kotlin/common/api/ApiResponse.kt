package dev.jcasas.money.common.api

sealed class ApiResponse {
    data class Success<T>(val data: T) : ApiResponse()
    data class Failure(val error: String) : ApiResponse()
}

fun <T> successResponse(data: T): ApiResponse.Success<T> {
    return ApiResponse.Success(data)
}

fun failureResponse(error: String? = null): ApiResponse.Failure {
    return ApiResponse.Failure(error ?: "There has been an error.")
}