package com.example.testingtddsimple

import android.accounts.NetworkErrorException
import com.example.testingtddsimple.featuresimpletdd.repository.UserRepository
import com.example.testingtddsimple.featuresimpletdd.usecase.GetUserNameUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

// Stub, are fake values created per hand
internal class UserRepositorySuccessImpl() : UserRepository{
    override fun getUserName(): Result<String> {
        return Result.success("John Doe")
    }
}


// Stub, are fake values created per hand
internal class UserRepositoryFailureImpl() : UserRepository{
    override fun getUserName(): Result<String> {
        return Result.failure(NetworkErrorException("Network Error Exception"))
    }
}


class GetUserNameUseCaseUnitTest {
    @Test
    fun `should return user name string when repository method getUserName() is called`(){
        // Arrange
        val repository = UserRepositorySuccessImpl()
        val useCase = GetUserNameUseCase(repository)
        // Act
        val result = useCase()
        // Assert
        Assert.assertEquals("John Doe", result.getOrNull())
    }

    @Test
    fun `should be Result isFailure when exception occurs`(){
        // Arrange
        val repository = UserRepositoryFailureImpl()
        val useCase = GetUserNameUseCase(repository)
        // Act
        val result = useCase()
        // Assert
        Assert.assertTrue(result.isFailure)
    }

    // mock with mockk
    @Test
    fun `should return user name string when repository method getUserName() is called, using mockk`(){
        // Arrange
        val repository = mockk<UserRepository>()
        // return Result.success("John Doe") every time that UserRepository is called
        // co every should actually start coroutines, or suspend functions
        coEvery { repository.getUserName() } returns Result.success("John Doe")
        val useCase = GetUserNameUseCase(repository)
        // Act
        val result = useCase()
        // Assert
        Assert.assertEquals("John Doe", result.getOrNull())
    }

    @Test
    fun `should return failure result when repository method getUserName() is called, using mockk`(){
        // Arrange
        val repository = mockk<UserRepository>()
        // return failure when getUserName is called
        coEvery { repository.getUserName() } returns Result.failure(Error("Error"))
        val useCase = GetUserNameUseCase(repository)
        // Act
        val result = useCase()
        // Assert
        Assert.assertEquals("Error", result.exceptionOrNull()?.message.toString())
    }




}