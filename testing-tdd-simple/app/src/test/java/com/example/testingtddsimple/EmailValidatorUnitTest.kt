package com.example.testingtddsimple

import com.example.testingtddsimple.featuresimpletdd.stringvalidator.EmailValidator
import org.junit.Assert
import org.junit.Test

class EmailValidatorUnitTest {

    @Test
    fun whenEmailIsValid_shouldReturnTrue(){
        val email = "myemail@mail.com"
        val result = EmailValidator.isEmailValid(email)
        //Assert.assertTrue(result)
        assert(result)
    }


    @Test
    fun whenEmailIsNotValid_shouldReturnFalse(){
        val email = "@com"
        val result = EmailValidator.isEmailValid(email)
        Assert.assertFalse(result)
    }

}