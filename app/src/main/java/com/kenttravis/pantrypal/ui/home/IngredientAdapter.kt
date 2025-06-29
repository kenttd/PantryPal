package com.kenttravis.pantrypal.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kenttravis.pantrypal.databinding.IngredientItemBinding

class IngredientDiffUtil: DiffUtil.ItemCallback<Map<String, Any>>(){
    override fun areItemsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
        return oldItem == newItem
    }
}

val ingredientDiffUtil = IngredientDiffUtil()

class IngredientAdapter: ListAdapter<Map<String, Any>, IngredientAdapter.ViewHolder>(
    ingredientDiffUtil
) {
    class ViewHolder(val binding: IngredientItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = IngredientItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        // Convert all key-value pairs into a string
        val builder = StringBuilder()
        for ((key, value) in data) {
            builder.append("$key = $value\n")
        }

        // Set the whole string to the TextView
        holder.binding.tvIngredientName.text = builder.toString().trim()
    }
}