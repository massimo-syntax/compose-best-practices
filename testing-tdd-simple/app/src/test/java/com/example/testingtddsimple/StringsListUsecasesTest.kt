package com.example.testingtddsimple

import com.example.testingtddsimple.featuremockk.AddStringUseCase
import com.example.testingtddsimple.featuremockk.DecorateStringUseCase
import com.example.testingtddsimple.featuremockk.EncryptStrings
import com.example.testingtddsimple.featuremockk.EncryptStringsUseCase
import com.example.testingtddsimple.featuremockk.LoadFromNetworkAndSaveLocallyUseCase
import com.example.testingtddsimple.featuremockk.NetworkCallbackUseCase
import com.example.testingtddsimple.featuremockk.RemoveAllUseCase
import com.example.testingtddsimple.featuremockk.RemoveStringUseCase
import com.example.testingtddsimple.featuremockk.StringDecorator
import com.example.testingtddsimple.featuremockk.StringsListRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkConstructor
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class StringsListUsecasesTest {
    @Test
    fun `when add string called repository should add string`() {
        // Arrange
        val repository = mockk<StringsListRepository>()
        val addUseCase = AddStringUseCase(repository)
        val myString = "abc"
        // define return value of repository.addString
        every { repository.addString(myString) } returns listOf(myString)

        // Act
        addUseCase(myString)

        // Assert
        // verify once
        verify(exactly = 1) { repository.addString(myString) }
        //  - fails
        //  addUseCase("sdlköfjklsadjf")
        //  verify(exactly = 1) { repository.addString(myString) }
        //  - fails
        //  addUseCase(myString)
        //  verify(exactly = 1) { repository.addString("lakjdfkla") }
    }

    @Test
    fun `when RemoveAll usecase is called repository should clear the list`() = runTest {
        // Arrange
        val repository = mockk<StringsListRepository>()
        val removeAllUseCase = RemoveAllUseCase(repository)
        // coEvery is the Every for coroutines, or suspend functions
        coEvery { repository.reset() } returns Unit

        // Act
        removeAllUseCase()

        // Assert
        // co - Verify is verify coroutines
        coVerify(exactly = 1) { repository.reset() }
        // fails when removeAllUseCase() is called more than once
    }

    @Test
    fun `when first adding, then removing a string, should call the respective repository functions in sequence`() {
        // Arrange
        val repository = mockk<StringsListRepository>()
        val addUseCase = AddStringUseCase(repository)
        val removeUseCase = RemoveStringUseCase(repository)
        val myString = "abc"

        every { repository.addString(myString) } returns listOf(myString)
        every { repository.removeString(myString) } returns listOf<String>()

        // Act
        addUseCase(myString)
        removeUseCase(myString)

        // Assert
        verifySequence {
            repository.addString(myString)
            repository.removeString(myString)
        }

    }

    @Test
    fun `when LoadFromNetworkAndSaveLocallyUseCase is called, repository loadStringsFromNetwork and saveStringsInDb are called in order`() = runTest {
        // Arrange
        val repository = mockk<StringsListRepository>()
        // val callback = {}
        val loadFromNetworkAndSaveUseCase = LoadFromNetworkAndSaveLocallyUseCase(repository)

        coEvery { repository.loadStringsFromNetwork() } returns Unit
        coEvery { repository.saveStringsInDb() } returns Unit

        // Act
        loadFromNetworkAndSaveUseCase()
        // Assert
        coVerifyOrder {
            repository.loadStringsFromNetwork()
            repository.saveStringsInDb()
        }
        // warning !
        // it fails with the callback!
        // see StringsListRepository
    }

    @Test
    fun `EncryptStrings singleton is mocked`(){
        val repository = mockk<StringsListRepository>()
        val encryptStringsUseCase = EncryptStringsUseCase(repository)
        val myString = "ldfjlak"

        every { repository.getStrings() } returns listOf(myString, myString)
        // mock EncryptStrings singleton
        // without it fails with error:
        // Missing mocked calls inside every { ... } block: make sure the object inside the block is a mock
        mockkObject(EncryptStrings)
        every { EncryptStrings.mocked } returns true

        encryptStringsUseCase()

        coVerify(exactly = 1) { repository.getStrings() }

        clearMocks(repository, EncryptStrings)

    }

    @Test
    fun `when decorating string should return decorated string`(){
        val repository = mockk<StringsListRepository>()
        val decorateStringUseCase= DecorateStringUseCase(repository)
        val myString = "dlkfjasldfj"
        mockkConstructor(StringDecorator::class)

        // function used in the use case
        every { repository.getStrings() } returns listOf(myString, myString)
        every { anyConstructed<StringDecorator>().decorateString(any()) } returns "alksj"

        val result = decorateStringUseCase()

        Assert.assertEquals("alksj", result)
        // free()
        unmockkConstructor(StringDecorator::class)
        clearMocks(repository)
    }

    @Test
    fun `when function with callback is called, callback is mocked`() = runTest {
        val repository = mockk<StringsListRepository>()
        val withCallbackUseCase = NetworkCallbackUseCase(repository)
        val myNetworkResponse = "hello, some network data or message"

        coEvery { repository.loadFromNetworkWithCallback(callback = any()) } coAnswers {
            // first argument, callback - as any()
            val callback = arg<suspend (String)-> Unit>(0)
            callback(myNetworkResponse)
        }

        // result that callback loads as parameter for onResult
        var result = ""
        withCallbackUseCase{ networkResponse ->
            result = networkResponse
        }

        Assert.assertEquals(myNetworkResponse, result)

    }


}

