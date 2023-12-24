package com.example.boilerplate.main.food_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FormAddFoodBinding
import com.example.boilerplate.databinding.FragmentFoodTrackerBinding
import com.example.boilerplate.main.food_tracker.model.Food
import com.example.boilerplate.manager.FirestoreManager
import com.google.android.material.bottomsheet.BottomSheetDialog

class FoodTrackerFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFoodTrackerBinding? = null
    private lateinit var viewModel: FoodViewModel

    private val binding get() = _binding!!
    private val food = mutableListOf<Food>()
    private lateinit var breakFastAdapter: FoodAdapter
    private lateinit var lunchAdapter: FoodAdapter
    private lateinit var dinnerAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodTrackerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        food.add(Food("Nasi Lemak", 20, "breakfast"))
        food.add(Food("lunch", 20, "lunch"))
        food.add(Food("dinner", 20, "dinner"))

        binding.breakfastRecyclerView.apply {
            breakFastAdapter = FoodAdapter(food, "breakfast")
            layoutManager = LinearLayoutManager(requireContext())
            adapter = breakFastAdapter
        }

        binding.lunchRecyclerView.apply {
            lunchAdapter = FoodAdapter(food, "lunch")
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lunchAdapter
        }

        binding.dinnerRecyclerView.apply {
            dinnerAdapter = FoodAdapter(food, "dinner")
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dinnerAdapter
        }

        viewModel.food.observe(viewLifecycleOwner) {
            food.clear()
            food.addAll(it)

            breakFastAdapter.notifyDataSetChanged()
            lunchAdapter.notifyDataSetChanged()
            dinnerAdapter.notifyDataSetChanged()
            updateCaloriesCount()
        }

        updateCaloriesCount()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBreakfast.setOnClickListener(this)
        binding.addLunch.setOnClickListener(this)
        binding.addDinner.setOnClickListener(this)
    }

    private fun updateCaloriesCount() {
        var totalBreakfast = 0L
        var totalLunch = 0L
        var totalDinner = 0L

        food.map {
            when (it.type) {
                "breakfast" -> totalBreakfast += it.calories
                "lunch" -> totalLunch += it.calories
                "dinner" -> totalDinner += it.calories
            }
        }

        binding.breakfastCalories.text = getString(R.string.calories_count, totalBreakfast)
        binding.lunchCalories.text = getString(R.string.calories_count, totalLunch)
        binding.dinnerCalories.text = getString(R.string.calories_count, totalDinner)

        binding.totalCalories.text =
            getString(R.string.totalCalories, totalBreakfast + totalLunch + totalDinner)
    }

    private fun dialogAddFood(type: String): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val addFoodDialog = FormAddFoodBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(addFoodDialog.root)

        addFoodDialog.btnAddFood.setOnClickListener {
            if (addFoodDialog.inputFoodName.editText!!.text.isNotEmpty() && addFoodDialog.inputCalories.editText!!.text.isNotEmpty()) {
                food.add(
                    Food(
                        addFoodDialog.inputFoodName.editText?.text.toString(),
                        addFoodDialog.inputCalories.editText?.text.toString().toLong(),
                        type
                    )
                )
                FirestoreManager().addFood(food.last())

                when (type) {
                    "breakfast" -> {
                        breakFastAdapter.notifyItemInserted(food.size +1)
                        updateCaloriesCount()
                    }

                    "lunch" -> {
                        lunchAdapter.notifyItemInserted(food.size +1)
                        updateCaloriesCount()
                    }

                    "dinner" -> {
                        dinnerAdapter.notifyItemInserted(food.size +1)
                        updateCaloriesCount()
                    }
                }

                bottomSheetDialog.dismiss()
            }
        }

        return bottomSheetDialog
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.addBreakfast.id -> dialogAddFood("breakfast").show()
            binding.addLunch.id -> dialogAddFood("lunch").show()
            binding.addDinner.id -> dialogAddFood("dinner").show()
        }
    }
}