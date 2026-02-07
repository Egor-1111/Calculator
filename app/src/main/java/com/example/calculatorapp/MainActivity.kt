    package com.example.calculatorapp

    import android.content.Intent
    import android.os.Build
    import android.os.Bundle
    import android.os.VibrationEffect
    import android.os.Vibrator
    import android.util.TypedValue
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.ViewModelProvider
    import com.example.calculatorapp.controllers.MainController
    import com.example.calculatorapp.databinding.ActivityMainBinding
    import com.example.calculatorapp.viewmodels.CalculatorViewModel
    import android.widget.EditText
    import com.example.calculatorapp.models.ThemeManager
    import com.example.calculatorapp.utils.SecurePinStorage
    class MainActivity : AppCompatActivity() {
    
        private lateinit var controller: MainController
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: CalculatorViewModel
    
        override fun onCreate(savedInstanceState: Bundle?) {

            when (ThemeManager.getTheme(this)) {
                ThemeManager.DARK -> setTheme(R.style.CalculatorTheme_Dark)
                ThemeManager.LIGHT -> setTheme(R.style.CalculatorTheme_Light)
            }

            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
    
            // Инициализация ViewModel
            viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]
    
            // Инициализация контроллера с ViewModel
            controller = MainController(this, viewModel)
    
            binding.imgBtnScientific.setOnClickListener {
                // Диалог для смены PIN
                val dialogView = layoutInflater.inflate(R.layout.dialog_change_pin, null)
                val currentPinInput = dialogView.findViewById<EditText>(R.id.currentPinInput)
                val newPinInput = dialogView.findViewById<EditText>(R.id.newPinInput)
                val confirmPinInput = dialogView.findViewById<EditText>(R.id.confirmPinInput)

                val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Смена PIN")
                    .setView(dialogView)
                    .setPositiveButton("Сохранить") { _, _ ->
                        val current = currentPinInput.text.toString()
                        val newPin = newPinInput.text.toString()
                        val confirm = confirmPinInput.text.toString()
                        val savedPin = SecurePinStorage.getPin(this)

                        when {
                            savedPin == null -> {
                                Toast.makeText(this, "PIN ещё не установлен", Toast.LENGTH_SHORT).show()
                            }
                            current != savedPin -> {
                                Toast.makeText(this, "Текущий PIN неверный", Toast.LENGTH_SHORT).show()
                            }
                            newPin != confirm -> {
                                Toast.makeText(this, "Новый PIN не совпадает с подтверждением", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                SecurePinStorage.savePin(this, newPin)
                                Toast.makeText(this, "PIN успешно изменён", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Отмена", null)
                    .create()

                dialog.show()
            }

            binding.imgBtnThemes.setOnClickListener {
                startActivity(
                    Intent(this, WeatherActivity::class.java)
                )
            }


            binding.imgBtnHistory.setOnClickListener {
                // Открываем экран истории
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
    
            // Настройка кнопок цифр
            setupNumberButtons()
    
            // Настройка кнопок операций
            setupOperatorButtons()
        }
    
        private fun setupNumberButtons() {
            binding.btnNum0.setOnClickListener { controller.onDigitClick("0") }
            binding.btnNum1.setOnClickListener { controller.onDigitClick("1") }
            binding.btnNum2.setOnClickListener { controller.onDigitClick("2") }
            binding.btnNum3.setOnClickListener { controller.onDigitClick("3") }
            binding.btnNum4.setOnClickListener { controller.onDigitClick("4") }
            binding.btnNum5.setOnClickListener { controller.onDigitClick("5") }
            binding.btnNum6.setOnClickListener { controller.onDigitClick("6") }
            binding.btnNum7.setOnClickListener { controller.onDigitClick("7") }
            binding.btnNum8.setOnClickListener { controller.onDigitClick("8") }
            binding.btnNum9.setOnClickListener { controller.onDigitClick("9") }
            binding.btnSymDecimal.setOnClickListener { controller.onDigitClick(".") }
        }
    
        private fun setupOperatorButtons() {
            binding.btnSymPlus.setOnClickListener { controller.onOperatorClick("+") }
            binding.btnSymMinus.setOnClickListener { controller.onOperatorClick("-") }
            binding.btnSymMultiply.setOnClickListener { controller.onOperatorClick("×") }
            binding.btnSymDivide.setOnClickListener { controller.onOperatorClick("÷") }
            binding.btnSymEqual.setOnClickListener { controller.onEqualClick() }
            binding.btnSymPercent.setOnClickListener { controller.onPercentClick() }
            binding.btnSymDel.setOnClickListener { controller.onBackspaceClick() }
            binding.btnSymClear.setOnClickListener { controller.onClearClick() }
        }
    
        fun vibrateOnClick() {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(30)
            }
        }
    
        fun updateDisplay(value: String) {
            binding.displayText.text = value
        }
    
        fun updatePreview(value: String) {
            binding.textPreview.text = value
        }
    
        fun displayError() {
            updateDisplay("Error")
        }
    
        fun resetAll() {
            controller.resetCurrentInput()
            binding.textPreview.text = ""
        }
    
        fun resetPreview() {
            binding.textPreview.text = ""
        }
    
        fun changeDisplaySize(value: Float) {
            binding.displayText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
    }