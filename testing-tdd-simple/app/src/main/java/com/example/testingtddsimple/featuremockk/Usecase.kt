package com.example.testingtddsimple.featuremockk

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class GetStringsUseCase(private val repo: StringsListRepository){
    operator fun invoke() = repo.getStrings()
}


class AddStringUseCase(private val repo: StringsListRepository){
    operator fun invoke(s: String) = repo.addString(s)
}

class RemoveStringUseCase(private val repo: StringsListRepository){
    operator fun invoke(s: String) = repo.removeString(s)
}


class RemoveAllUseCase(private val repo: StringsListRepository){
    suspend operator fun invoke() = repo.reset()
}

class PersistIntoDb(private val repo: StringsListRepository){
    suspend operator fun invoke() = repo.saveStringsInDb()
}

class EncryptStringsUseCase(private val repo: StringsListRepository){
     operator fun invoke(): List<String> {
        val strings = repo.getStrings()
        val crypted = EncryptStrings.encrypt(strings)
        return crypted
    }
}
class LoadFromNetworkAndSaveLocallyUseCase(
    private val repo: StringsListRepository,
    //private val callback: ()->Unit = {}
){
    suspend operator fun invoke(){
        repo.loadStringsFromNetwork() //{ callback() }
        repo.saveStringsInDb()
    }
}