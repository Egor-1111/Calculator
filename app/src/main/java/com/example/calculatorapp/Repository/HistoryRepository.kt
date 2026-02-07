package com.example.calculatorapp.repositories

import android.util.Log
import com.example.calculatorapp.models.CalculationHistory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HistoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "calculations_history"

    /**
     * Сохранить операцию в историю
     */
    fun saveCalculation(expression: String, result: String, deviceId: String) {
        val calculation = CalculationHistory(
            expression = expression,
            result = result,
            deviceId = deviceId
        )

        db.collection(collectionName)
            .add(calculation)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "✅ Calculation saved: $expression = $result")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Error saving calculation", e)
            }
    }

    /**
     * Загрузить историю для устройства
     */
    fun loadHistory(deviceId: String, onSuccess: (List<CalculationHistory>) -> Unit) {
        db.collection(collectionName)
            .whereEqualTo("deviceId", deviceId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50) // Ограничим 50 последними записями
            .get()
            .addOnSuccessListener { documents ->
                val historyList = mutableListOf<CalculationHistory>()

                for (document in documents) {
                    val calculation = document.toObject(CalculationHistory::class.java)
                        .copy(id = document.id) // Сохраняем ID документа
                    historyList.add(calculation)
                }

                Log.d("Firestore", "📥 Loaded ${historyList.size} calculations")
                onSuccess(historyList)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Error loading history", e)
                onSuccess(emptyList())
            }
    }

    /**
     * Очистить историю для устройства
     */
    fun clearHistory(deviceId: String, onComplete: () -> Unit) {
        db.collection(collectionName)
            .whereEqualTo("deviceId", deviceId)
            .get()
            .addOnSuccessListener { documents ->
                val batch = db.batch()

                for (document in documents) {
                    batch.delete(document.reference)
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("Firestore", "🧹 History cleared")
                        onComplete()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "❌ Error clearing history", e)
                        onComplete()
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Error getting documents to clear", e)
                onComplete()
            }
    }
}