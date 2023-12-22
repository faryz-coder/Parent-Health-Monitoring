package com.example.boilerplate.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentSignupBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import kotlin.time.Duration.Companion.days

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        instantiateDatePicker()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }

        binding.btnSelectDate.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "dateOfBirth")
        }

    }

    private fun instantiateDatePicker() {
        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date Of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = formatDate(it)
            Log.d("Bmh","instantiateDatePicker: it::$it, selection::${selectedDate}")
            // Set Date to Button Text
            binding.btnSelectDate.text = selectedDate
        }
    }

    private fun formatDate(milliseconds: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
        val year = calendar.get(Calendar.YEAR)

        return "$day/$month/$year"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}