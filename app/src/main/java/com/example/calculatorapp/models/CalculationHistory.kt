package com.example.calculatorapp.models

import android.content.Context
import android.text.format.DateFormat
import java.util.Date

data class CalculationHistory(
    val id: String = "",           // Автоматически генерируется Firestore
    val expression: String = "",    // "2 + 2 × 3"
    val result: String = "",        // "8"
    val timestamp: Long = System.currentTimeMillis(),
    val deviceId: String = "",      // Для идентификации устройства
) {
    // Форматирование даты для отображения (теперь требует Context)
    fun getFormattedDate(context: Context): String {
        return try {
            val date = Date(timestamp)
            DateFormat.getDateFormat(context).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun getFormattedTime(context: Context): String {
        return try {
            val date = Date(timestamp)
            DateFormat.getTimeFormat(context).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /* Метод без контекста для Firebase (не требует форматирования)
    fun getSimpleTimestamp(): String {
        return Date(timestamp).toString()
    }

     */
}