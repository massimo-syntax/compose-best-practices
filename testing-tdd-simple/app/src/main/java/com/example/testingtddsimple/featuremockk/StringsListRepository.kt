package com.example.testingtddsimple.featuremockk

interface StringsListRepository {
    fun getStrings(): List<String>
    fun addString(s:String): List<String>
    fun removeString(s:String): List<String>
    suspend fun reset()
    suspend fun saveStringsInDb()
    suspend fun loadStringsFromNetwork(/*callback: ()->Unit*/)
    suspend fun loadFromNetworkWithCallback(callback: suspend (String)->Unit)
}