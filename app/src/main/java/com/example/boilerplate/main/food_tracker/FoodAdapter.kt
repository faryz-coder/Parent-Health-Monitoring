package com.example.boilerplate.main.food_tracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boilerplate.databinding.RowFoodBinding
import com.example.boilerplate.main.food_tracker.model.Food

class FoodAdapter(private val food: MutableList<Food>, val type: String) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    class ViewHolder(val binding: RowFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return food.count { it.type == type }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodType = food.filter { it.type == type }
        val item = foodType[position]

        if (item.type == type) {
            holder.binding.foodNo.text = buildString {
                append(position + 1)
            }
            holder.binding.foodName.text = item.name
            holder.binding.foodCalories.text = item.calories.toString()
        }
    }

}
