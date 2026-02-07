package com.example.calculatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorapp.utils.SecurePinStorage

class SetupPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_pin)

        val pinInput = findViewById<EditText>(R.id.pinInput)
        val btnSave = findViewById<Button>(R.id.btnSavePin)

        btnSave.setOnClickListener {
            val pin = pinInput.text.toString()

            if (pin.length != 4) {
                Toast.makeText(this, "PIN должен быть из 4 цифр", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SecurePinStorage.savePin(this, pin)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
