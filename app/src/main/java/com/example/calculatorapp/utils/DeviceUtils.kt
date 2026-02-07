package com.example.calculatorapp.utils

import android.content.Context
import android.provider.Settings
import java.util.UUID

object DeviceUtils {
    /**
     * Получить уникальный ID устройства
     */
    fun getDeviceId(context: Context): String {
        return try {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ) ?: UUID.randomUUID().toString()
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }
}