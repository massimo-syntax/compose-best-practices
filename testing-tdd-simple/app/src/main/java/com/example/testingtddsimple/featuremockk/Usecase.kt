package com.example.testingtddsimple.featuremockk

// iv heard that when there are many related use cases can be better to have just 1 class
// that is for testing purposes

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

class DecorateStringUseCase(private val repository: StringsListRepository){
    // not good to test invoke function mocking decorator
    // of course when a class is better to have the object from the constructor,was just fun debugging:)
    // ...eh because can also be that you need the same one, then being an experienced developer i find hilt cook
    // private val decorator = StringDecorator()
    operator fun invoke(): String{
        val decorator = StringDecorator()
        val string = repository.getStrings().last()
        return decorator.decorateString(string)
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

class NetworkCallbackUseCase(
    private val repository: StringsListRepository,
){
    suspend operator fun invoke( onResult: (String)->Unit ){
        repository.loadFromNetworkWithCallback{ str -> onResult(str) }
    }
}