package com.example.testdataflowviewmodelnetwork.domain.result

sealed interface FetchError {
    data object NetworkError : FetchError
    data object ServerError : FetchError
    data object UnknownError: FetchError
}