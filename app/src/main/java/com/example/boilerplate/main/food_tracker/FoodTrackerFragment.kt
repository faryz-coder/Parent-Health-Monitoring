package com.example.boilerplate.main.food_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.boilerplate.databinding.FragmentFoodTrackerBinding

class FoodTrackerFragment : Fragment(){
    private var _binding: FragmentFoodTrackerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodTrackerBinding.inflate(inflater, container, false)

        return binding.root
    }
}