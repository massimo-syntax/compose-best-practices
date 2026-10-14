package com.example.testdataflowviewmodelnetwork.domain.result

sealed interface FetchResult<out T> {
    data class Success<T>(val data: T) : FetchResult<T>
    data class Failure(val error: FetchError) : FetchResult<Nothing>
}