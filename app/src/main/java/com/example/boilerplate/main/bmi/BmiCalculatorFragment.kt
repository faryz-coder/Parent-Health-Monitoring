package com.example.boilerplate.main.bmi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentBmiBinding
import com.example.boilerplate.utils.UtilsInterface
import com.google.android.material.slider.Slider
import kotlin.math.sqrt

class BmiCalculatorFragment : Fragment(), View.OnClickListener, UtilsInterface {
    private var _binding: FragmentBmiBinding? = null

    private val binding get() = _binding!!
    private var weight = 0F
    private var height: Float = 0F
    private var age = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)

        binding.bmiHeight.text = getString(R.string.input_cm, height.toString())


        binding.heightBar.addOnChangeListener { _, value, _ ->
            // Responds to when slider's value is changed
            Log.d("BmiCalculatorFragment", "value: $value")
            height = value
            binding.bmiHeight.text = getString(R.string.input_cm, height.toString())
        }

        reset()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bmiWeightMinus.setOnClickListener(this)
        binding.bmiWeightAdd.setOnClickListener(this)
        binding.bmiAgeAdd.setOnClickListener(this)
        binding.bmiAgeMinus.setOnClickListener(this)
        binding.btnCalculateBmi.setOnClickListener(this)
        binding.btnResetBmi.setOnClickListener(this)
        binding.bmiCalculatorLayout.setOnClickListener(this)
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.bmiWeightMinus.id -> setWeight(false)
            binding.bmiWeightAdd.id -> setWeight(true)
            binding.bmiAgeMinus.id -> setAge(false)
            binding.bmiAgeAdd.id -> setAge(true)
            binding.btnResetBmi.id -> reset()
            binding.btnCalculateBmi.id -> calculateBmi()
            binding.bmiCalculatorLayout.id -> hideKeyboard(requireActivity(), requireView().findFocus())
        }
    }

    private fun calculateBmi() {
        weight = binding.bmiWeight.editText!!.text.toString().toFloat()
        val heightInMeters = height/100
        val squareOfHeight = heightInMeters * heightInMeters
        val bmi =  weight / squareOfHeight

        if (bmi < 16.0F) {
            binding.bmiStatus.text = getString(R.string.you_are_severely_underweight)
        } else if (bmi < 18.5F ) {
            binding.bmiStatus.text = getString(R.string.you_are_underweight)
        } else if (bmi < 25.0F) {
            binding.bmiStatus.text = getString(R.string.you_are_normal)
        } else if (bmi < 30.0F) {
            binding.bmiStatus.text = getString(R.string.you_are_overweight)
        } else if (bmi < 35.0F) {
            binding.bmiStatus.text = getString(R.string.you_are_moderately_obese)
        } else if (bmi > 35.0F) {
            binding.bmiStatus.text = getString(R.string.you_are_severely_obese)
        }
    }

    private fun setAge(input: Boolean) {
        if (input) {
            age += 1F
            binding.bmiAge.editText!!.setText(age.toString())
        } else {
            age -= 1F
            binding.bmiAge.editText!!.setText(age.toString())
        }
    }

    private fun setWeight(input: Boolean) {
        if (input) {
            weight += 1F
            binding.bmiWeight.editText!!.setText(weight.toString())
        } else {
            weight -= 1F
            binding.bmiWeight.editText!!.setText(weight.toString())
        }
    }

    private fun reset() {
        age = 0F
        weight = 0F
        height = 0F

        binding.bmiWeight.editText!!.setText("0")
        binding.bmiAge.editText!!.setText("0")
        binding.heightBar.value = 0F
    }
}