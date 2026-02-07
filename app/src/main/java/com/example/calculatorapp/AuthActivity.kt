package com.example.calculatorapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.calculatorapp.utils.SecurePinStorage

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Если PIN нет — создаём
        if (!SecurePinStorage.hasPin(this)) {
            startActivity(Intent(this, SetupPinActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_auth)

        showBiometric()
        setupPinInput()
    }

    private fun showBiometric() {
        val prompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    openMain()
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Вход в калькулятор")
            .setSubtitle("Отпечаток пальца или PIN")
            .setNegativeButtonText("Ввести PIN")
            .build()

        prompt.authenticate(info)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }

    private fun setupPinInput() {
        val pinInput = findViewById<EditText>(R.id.pinInput)

        pinInput.setOnEditorActionListener { _, _, _ ->
            val entered = pinInput.text.toString()
            val saved = SecurePinStorage.getPin(this)

            if (entered == saved) {
                openMain()
            } else {
                Toast.makeText(this, "Неверный PIN", Toast.LENGTH_SHORT).show()
                pinInput.text.clear()
            }
            true
        }
    }

    private fun openMain() {
        App.isLocked = false
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
