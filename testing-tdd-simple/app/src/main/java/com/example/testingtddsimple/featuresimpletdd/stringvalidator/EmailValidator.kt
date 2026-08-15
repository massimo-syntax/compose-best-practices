package com.example.testingtddsimple.featuresimpletdd.stringvalidator

object EmailValidator {
    fun isEmailValid(email: String) =
        email.contains('@')
                && email.contains('.')
                && !email.startsWith('@')
                && !email.endsWith('.')
}