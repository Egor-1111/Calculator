package com.example.calculatorapp.controllers

import com.example.calculatorapp.MainActivity
import com.example.calculatorapp.models.MainModel
import com.example.calculatorapp.viewmodels.CalculatorViewModel

class MainController(    private val mainView: MainActivity,
                         private val viewModel: CalculatorViewModel) {

    var mainModel: MainModel = MainModel()


    private fun feedback() {
        mainView.vibrateOnClick()
    }



    fun onDigitClick(digit: String){

        feedback()

        val isValid = mainModel.isValidDigit(digit)

        if (isValid) {

            mainModel.fixCurrentInputDigit(digit)
            mainModel.appendToCurrentInput(digit)

            mainView.resetPreview()

            mainModel.deleteCharsFromCurrentInput(',')
            mainView.updateDisplay( mainModel.currentInput.toString() )

        }

        mainView.changeDisplaySize( calculateDisplayChange() )

    }

    fun onOperatorClick(operator: String) {

        val isValid = mainModel.isValidOperator()

        if (isValid) {

            mainModel.fixCurrentInputOperator()
            mainModel.appendToCurrentInput(operator)

            mainView.resetPreview()

            mainModel.deleteCharsFromCurrentInput(',')
            mainView.updateDisplay( mainModel.currentInput.toString() )

        }

        mainView.changeDisplaySize( calculateDisplayChange() )

    }

    fun onPercentClick() {

        val isValid = mainModel.isValidPercent()

        if (isValid){

            mainModel.deleteCharsFromCurrentInput(',')

            mainModel.appendToCurrentInput("÷")
            mainModel.appendToCurrentInput("100")

            val expression = mainModel.currentInput.toString()

            val result = mainModel.evaluateExpression(expression)

            if (result == "error"){

                mainView.displayError()
                mainView.resetAll()
                return

            }

            mainModel.currentInput.clear()
            mainModel.appendToCurrentInput(result)

            mainView.resetPreview()
            mainView.updateDisplay(result)

        }

    }

    fun onEqualClick() {

        val isValid = mainModel.isValidEqual()

        if(isValid) {

            mainView.updatePreview(mainModel.currentInput.toString() + "=")

            val expression = mainModel.currentInput.toString()

            val result = mainModel.evaluateExpression(expression)

            if (result == "error") {

                mainView.displayError()
                mainView.resetAll()
                return

            }
            else if (result == "infinity_error") {

                mainView.updateDisplay("∞")
                mainModel.currentInput.clear()
                mainModel.currentInput.append(0)
                return

            }

            mainModel.currentInput.clear()
            mainModel.appendToCurrentInput(result)

            mainView.updateDisplay(result)
            viewModel.saveCalculation(expression, result)

        }

        mainView.changeDisplaySize( calculateDisplayChange() )

    }

    fun onBackspaceClick() {

        mainView.resetPreview()

        if ( mainModel.currentInput.length == 1 || ( mainModel.currentInput.length == 2 && mainModel.currentInput.first() == '-' ) ){

            mainView.resetAll()

            mainModel.deleteCharsFromCurrentInput(',')
            mainView.updateDisplay( mainModel.currentInput.toString() )

        }

        else {

            mainModel.deleteCharFromCurrentInputAt(mainModel.currentInput.length - 1)

            mainModel.deleteCharsFromCurrentInput(',')
            mainView.updateDisplay( mainModel.currentInput.toString() )

        }

        mainView.changeDisplaySize( calculateDisplayChange() )

    }

    fun onClearClick() {

        mainView.resetAll()
        mainView.updateDisplay( mainModel.currentInput.toString() )

        mainView.changeDisplaySize( calculateDisplayChange() )

    }

    fun resetCurrentInput(){

        mainModel.currentInput.clear()
        mainModel.currentInput.append("0")

    }

    private fun calculateDisplayChange(): Float{

        if(mainModel.currentInput.length > 18) return 28f

        else if(mainModel.currentInput.length > 16) return 30f

        else if(mainModel.currentInput.length > 12) return 35f

        return 40f

    }

}