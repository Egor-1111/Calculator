package com.example.calculatorapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.calculatorapp.models.CalculationHistory
import com.example.calculatorapp.repositories.HistoryRepository
import com.example.calculatorapp.utils.DeviceUtils

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HistoryRepository()
    private val deviceId = DeviceUtils.getDeviceId(application)

    val history = MutableLiveData<List<CalculationHistory>>()


    fun saveCalculation(expression: String, result: String) {
        repository.saveCalculation(expression, result, deviceId)
    }


    fun loadHistory() {
        repository.loadHistory(deviceId) { historyList ->
            history.postValue(historyList)
        }
    }

    fun clearHistory(onComplete: () -> Unit) {
        repository.clearHistory(deviceId) {
            loadHistory()
            onComplete()
        }
    }
}