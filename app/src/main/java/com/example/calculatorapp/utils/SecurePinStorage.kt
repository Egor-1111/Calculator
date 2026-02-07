package com.example.calculatorapp.utils
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePinStorage {

    private const val PREFS_NAME = "secure_prefs"
    private const val PIN_KEY = "user_pin"

    private fun prefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun savePin(context: Context, pin: String) {
        prefs(context).edit().putString(PIN_KEY, pin).apply()
    }

    fun getPin(context: Context): String? {
        return prefs(context).getString(PIN_KEY, null)
    }

    fun hasPin(context: Context): Boolean {
        return getPin(context) != null
    }

    fun clearPin(context: Context) {
        prefs(context).edit().remove(PIN_KEY).apply()
    }
}
