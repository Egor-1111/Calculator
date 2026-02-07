package com.example.calculatorapp

fun isBinaryOperator(token: String): Boolean { return token in setOf( "+", "-", "×", "÷", "^" ) }