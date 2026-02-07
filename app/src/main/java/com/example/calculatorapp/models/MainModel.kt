package com.example.calculatorapp.models

import android.util.Log
import com.example.calculatorapp.Calculator
import com.example.calculatorapp.Formatter
import com.example.calculatorapp.containsAnyOperator
import com.example.calculatorapp.isBinaryOperator
import java.lang.StringBuilder

class MainModel{

    var currentInput = StringBuilder("0")

    val formatter = Formatter()
    val calculator = Calculator()

    fun isValidDigit(digit: String): Boolean{

        val lastOperand = currentInput.split( Regex("[+\\-×÷^]") ).last()

        if ( digit == "0" && currentInput.toString() == "0" ) return false
        if ( digit == "0" && lastOperand.startsWith("0" ) && !lastOperand.contains(".") ) return false

        if ( digit == "." && isBinaryOperator(currentInput.last().toString() )
            || digit == "." && lastOperand.contains(".") ) return false

        return true

    }

    fun isValidOperator(): Boolean{

        if ( currentInput.last().toString() == "." ) return false

        return true

    }

    fun isValidPercent(): Boolean{

        if ( currentInput.toString() == "0" || currentInput.last() == '.' || isBinaryOperator( currentInput.last().toString() ) ) return false

        return true

    }

    fun isValidEqual(): Boolean{

        if ( currentInput.toString() == "0" || currentInput.last() == '.') return false

        else if ( isBinaryOperator( currentInput.last().toString() )
            || !currentInput.toString().containsAnyOperator() ) return false

        return true

    }

    fun fixCurrentInputDigit(digit: String){

        val lastOperand = currentInput.split( Regex("[+\\-×÷^]") ).last()

        if ( digit != "." && lastOperand.startsWith("0" ) && !lastOperand.contains(".") ) currentInput.deleteCharAt(currentInput.length - 1 )

        else if ( digit != "." && currentInput.toString() == "0" ) currentInput.deleteCharAt(0)

    }

    fun fixCurrentInputOperator(){

        if( isBinaryOperator( currentInput.last().toString() ) ) currentInput.deleteCharAt(currentInput.length - 1)

    }

    fun appendToCurrentInput(value: String){

        currentInput.append(value)

    }

    fun deleteCharFromCurrentInputAt(index: Int){

        currentInput.deleteCharAt(index)

    }

    fun deleteCharsFromCurrentInput(char: Char) {

        if ( currentInput.contains(char) ){

            while (currentInput.contains(char)){

                val index = currentInput.indexOf(char)

                Log.d("check", "Character index: $index")
                Log.d("check", "Input: $currentInput")

                currentInput.deleteCharAt(index)

            }

        }

    }

    fun evaluateExpression(expression: String): String{

        val tokens = formatter.tokenizeExpression(expression)

        val calculation = calculator.calculateWithPrecedence(tokens)

        val result = formatter.beautify(calculation)

        return result

    }


}