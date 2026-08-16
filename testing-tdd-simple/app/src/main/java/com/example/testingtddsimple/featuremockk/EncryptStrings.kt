package com.example.testingtddsimple.featuremockk

import kotlin.random.Random

object EncryptStrings {
    fun encrypt(sgs: List<String>): List<String>{
        return sgs.apply {
            map{"${repeat(10){ Random.nextInt() }}"}
        }
    }

    var mocked: Boolean = false
}