package com.example.testingtddsimple.featuresimpletdd.usecase

import com.example.testingtddsimple.featuresimpletdd.repository.UserRepository

class GetUserNameUseCase(private val repository: UserRepository) {
    operator fun invoke() = repository.getUserName()
}