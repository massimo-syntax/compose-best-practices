package com.example.workmanager.data

import jakarta.inject.Inject
import kotlinx.coroutines.delay

class RemoteDataSource @Inject constructor() {

    suspend fun networkRequest(): String{
        delay(2000)
        return "id: 123, network data: data, source: ${RemoteDataSource::class.java}"
    }

}