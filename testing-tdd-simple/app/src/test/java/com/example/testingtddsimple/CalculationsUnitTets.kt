package com.example.testingtddsimple

import com.example.testingtddsimple.featuresimpletdd.calculations.Calculator
import org.junit.Assert
import org.junit.Test

class CalculationsUnitTets {

    @Test
    fun whenTwoIntegersAreAdded_shouldReturnCorrectResult(){
        // Arrange
        val calculator = Calculator()
        val firstNumber = 1
        val secondNumber = 2
        // Act
        val sum = calculator.add(firstNumber, secondNumber)
        // Assert
        Assert.assertEquals(3, sum)
    }

    @Test
    fun whenTwoIntegersAreSubtracted_shouldReturnCorrectResult(){
        // Arrange
        val calculator = Calculator()
        val firstNumber = 100
        val secondNumber = 99
        // Act
        val subtraction = calculator.subtract(firstNumber, secondNumber)
        // Assert
        Assert.assertEquals(1, subtraction)
    }

    // naming convention Given When Then
    @Test
    fun givenTwoIntegers_whenAdded_shouldReturnCorrectResult(){
        // Arrange
        val calculator = Calculator()
        val firstNumber = 1
        val secondNumber = 2
        // Act
        val sum = calculator.add(firstNumber, secondNumber)
        // Assert
        Assert.assertEquals(3, sum)
    }

    // descriptive naming convention
    @Test
    fun `when 2 integers are added should return correct result`(){
        // Arrange
        val calculator = Calculator()
        // Act
        val sum = calculator.add(33, 66)
        // Assert
        Assert.assertEquals(99, sum)
    }

}