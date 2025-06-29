package com.kenttravis.pantrypal.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kenttravis.pantrypal.databinding.NutrimentItemBinding

class NutrimentsDiffUtil: DiffUtil.ItemCallback<Pair<String, Any>>(){
    override fun areItemsTheSame(oldItem: Pair<String, Any>, newItem: Pair<String, Any>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pair<String, Any>, newItem: Pair<String, Any>): Boolean {
        return oldItem == newItem
    }
}

val nutrimentsDiffutil = NutrimentsDiffUtil()

class NutrimentsAdapter: ListAdapter<Pair<String, Any>, NutrimentsAdapter.ViewHolder>(
    nutrimentsDiffutil
) {
    class ViewHolder(val binding: NutrimentItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NutrimentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (key, value) = getItem(position)
        holder.binding.tvNutrimentName.text = key
        holder.binding.tvNutrimentValue.text = value.toString()
    }
}