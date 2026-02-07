package com.example.calculatorapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculatorapp.databinding.ItemCalculationBinding
import com.example.calculatorapp.models.CalculationHistory

class HistoryAdapter(
    private val context: android.content.Context,
    private val onItemClick: (CalculationHistory) -> Unit
) : ListAdapter<CalculationHistory, HistoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(
        private val binding: ItemCalculationBinding,
        private val onItemClick: (CalculationHistory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: CalculationHistory) {
            binding.textExpression.text = calculation.expression
            binding.textResult.text = "= ${calculation.result}"

            // Форматируем дату
            val date = calculation.getFormattedDate(binding.root.context)
            val time = calculation.getFormattedTime(binding.root.context)
            binding.textTimestamp.text = "$time, $date"

            binding.root.setOnClickListener {
                onItemClick(calculation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalculationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CalculationHistory>() {
        override fun areItemsTheSame(oldItem: CalculationHistory, newItem: CalculationHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CalculationHistory, newItem: CalculationHistory): Boolean {
            return oldItem == newItem
        }
    }
}