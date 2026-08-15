package com.example.testingtddsimple.featuresimpletdd.repository

interface UserRepository {
    fun getUserName(): Result<String>
}