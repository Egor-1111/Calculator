package com.example.calculatorapp

import android.util.Log

class Calculator {


    fun calculateWithPrecedence(tokens: MutableList<String>) : String{

        val calculatedTokens = mutableListOf<String>()
        var i = 0

        while (i < tokens.size) {

            val token = tokens[i]

            if ( isBinaryOperator(token) ) {

                val nextFirstOperand = if (i + 1 < tokens.size) tokens[i + 1] else ""
                val nextOperator = if (i + 2 < tokens.size) tokens[i + 2] else ""
                val nextSecondOperand = if (i + 3 < tokens.size) tokens[i + 3] else ""

                // If the next operation has higher precedence, calculate the next operation
                if ( nextOperator.isNotEmpty() && ( getPrecedence(nextOperator) > getPrecedence(token) ) ) {

                    calculatedTokens.add(token)

                    val result = performOperation(nextFirstOperand.toDouble(), nextSecondOperand.toDouble() , nextOperator)
                    calculatedTokens.add(result.toString())

                    i += 4

                }

                else if( nextOperator.isNotEmpty() && ( getPrecedence(nextOperator) < getPrecedence(token) ) ){

                    val result = performOperation( calculatedTokens.removeAt( calculatedTokens.size - 1 ).toDouble(), nextFirstOperand.toDouble() , token)
                    calculatedTokens.add(result.toString())
                    i += 2

                }

                else {

                    val result = performOperation( calculatedTokens.removeAt( calculatedTokens.size - 1 ).toDouble(), nextFirstOperand.toDouble() , token)
                    calculatedTokens.add(result.toString())
                    i += 2

                }

            }
            else {
                calculatedTokens.add(tokens[i])
                i++
            }

            Log.d("check", "Iteration - $i, List: $calculatedTokens")

        }

        // Finish up operations
        try {
            while(calculatedTokens.size > 1){

                val operand1 = calculatedTokens.removeFirst()
                val operator = calculatedTokens.removeFirst()
                val operand2 = calculatedTokens.removeFirst()

                val result = performOperation(operand1.toDouble(), operand2.toDouble(), operator)
                calculatedTokens.add(0, result.toString() )

            }
        }
        catch (e: Exception) { return "error" }

        Log.d("check", "Completed Calc: $calculatedTokens")
        return calculatedTokens.toString().removePrefix("[").removeSuffix("]")

    }

    private fun performOperation(firstOperand: Double, secondOperand: Double, operator: String): Double {

        return when (operator) {

            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand

            "×" -> firstOperand * secondOperand
            "÷" -> firstOperand / secondOperand

            else -> throw IllegalArgumentException("Unknown operator: $operator")

        }

    }


    private fun getPrecedence(operator: String): Int {

        return when (operator) {
            "^" -> 3
            "×", "÷" -> 2
            "+", "-" -> 1
            else -> 0
        }

    }

}