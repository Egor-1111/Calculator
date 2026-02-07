package com.example.calculatorapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculatorapp.adapters.HistoryAdapter
import com.example.calculatorapp.databinding.ActivityHistoryBinding
import com.example.calculatorapp.viewmodels.CalculatorViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка тулбара
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "История операций"

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

        // Настройка RecyclerView
        adapter = HistoryAdapter(this) { calculation ->
            copyToClipboard("${calculation.expression} = ${calculation.result}")
        }

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistory.adapter = adapter

        // Наблюдение за историей
        viewModel.history.observe(this) { historyList ->
            updateUI(historyList)
        }

        // Кнопки
        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory {
                Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRefresh.setOnClickListener {
            viewModel.loadHistory()
            Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show()
        }

        // Загружаем историю при запуске
        viewModel.loadHistory()
    }

    private fun updateUI(historyList: List<com.example.calculatorapp.models.CalculationHistory>) {
        if (historyList.isEmpty()) {
            binding.textEmptyHistory.visibility = View.VISIBLE
            binding.recyclerViewHistory.visibility = View.GONE
        } else {
            binding.textEmptyHistory.visibility = View.GONE
            binding.recyclerViewHistory.visibility = View.VISIBLE
            adapter.submitList(historyList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Calculation", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Скопировано в буфер", Toast.LENGTH_SHORT).show()
    }
}