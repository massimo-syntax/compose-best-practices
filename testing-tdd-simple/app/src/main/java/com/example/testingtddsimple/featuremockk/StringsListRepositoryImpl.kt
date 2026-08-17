package com.example.testingtddsimple.featuremockk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object MyDb{
    var list = mutableListOf<String>()
}

class StringsListRepositoryImpl(private val database: MyDb = MyDb) : StringsListRepository {
    private var list = mutableListOf<String>()

    override fun getStrings(): List<String> = list.toList()

    override fun addString(s: String): List<String> =
        list.let {
            it.add(s)
            it
        }

    override fun removeString(s: String): List<String> =
        with(list){
            if(contains(s)) remove(s)
            this
        }

    override suspend fun reset() {
        list = mutableListOf<String>()
    }

    override suspend fun saveStringsInDb() {
        withContext(Dispatchers.IO){
            database.list = list
        }
    }

    override suspend fun loadStringsFromNetwork(/*callback: ()->Unit*/) {
        delay(1000)
        //callback()
    }

    override suspend fun loadFromNetworkWithCallback(callback: suspend (String) -> Unit) {
        withContext(Dispatchers.IO) {
            val networkResponse = "hello from here"
            callback(networkResponse)
        }
    }
}