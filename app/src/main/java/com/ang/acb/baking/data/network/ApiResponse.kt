package com.ang.acb.baking.data.network

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
 *
 * See: https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
sealed class ApiResponse<T> {

    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "Unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body = body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) response.message() else msg

                ApiErrorResponse(errorMsg ?: "Unknown error")
            }
        }
    }
}

/**
 * The API response when the HTTP status code in 204 (no content)
 * and the response body is null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

/**
 * The API response when the HTTP status code in the range [200..300)
 * and the response body is non-null.
 */
data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

/**
 * The API response when the HTTP status code is greater than 400
 * (like 4xx for Client Error, or 5xx for a Server Error).
 */
data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
