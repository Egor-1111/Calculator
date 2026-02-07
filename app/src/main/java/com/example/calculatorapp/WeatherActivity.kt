package com.example.calculatorapp


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.calculatorapp.databinding.ActivityWeatherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    private val API_KEY = "1a31e2a0cc374e17ba7155246262801"
    private val CITY = "Minsk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка назад
        binding.btnBack?.setOnClickListener {
            finish()
        }

        loadWeather()
    }

    private fun loadWeather() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    URL(
                        "https://api.weatherapi.com/v1/current.json" +
                                "?key=$API_KEY&q=$CITY&lang=ru"
                    ).readText()
                }

                val json = JSONObject(response)
                val current = json.getJSONObject("current")
                val location = json.getJSONObject("location")

                val temp = current.getDouble("temp_c")
                val condition = current.getJSONObject("condition").getString("text")

                binding.tvCity?.text = location.getString("name")
                binding.tvTemp?.text = "$temp°C"
                binding.tvCondition?.text = condition

            } catch (e: Exception) {
                Toast.makeText(
                    this@WeatherActivity,
                    "Ошибка загрузки погоды",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
