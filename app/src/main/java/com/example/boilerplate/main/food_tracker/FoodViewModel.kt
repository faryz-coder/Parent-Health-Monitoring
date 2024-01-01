package com.example.boilerplate.main.food_tracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boilerplate.main.food_tracker.model.Food
import com.example.boilerplate.manager.FirestoreManager

class FoodViewModel : ViewModel() {

    init {
        FirestoreManager().getListFood(::setListFood)
    }

    private val _food = MutableLiveData<MutableList<Food>>()

    val food = _food

    private fun setListFood(food: MutableList<Food>) {
        _food.value?.clear()
        _food.value = food
    }

    fun removeFood(id: String) {
        FirestoreManager().removeFood(id, ::refreshFood)
    }

    fun refreshFood() {
        FirestoreManager().getListFood(::setListFood)
    }

}