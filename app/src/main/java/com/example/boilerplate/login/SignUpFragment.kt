package com.example.boilerplate.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentSignupBinding
import com.example.boilerplate.login.model.SignUpInfo
import com.example.boilerplate.main.MainActivity
import com.example.boilerplate.manager.AuthManager
import com.example.boilerplate.utils.UtilsInterface
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.time.Duration.Companion.days

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment(), UtilsInterface, View.OnClickListener {

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

        binding.signUpLayout.setOnClickListener(this)

        binding.btnSelectDate.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "dateOfBirth")
        }

        binding.btnCreateAccount.setOnClickListener {
            progressIndicator()
            if (validate()) {
                AuthManager().createUser(signupInfo(), ::onSuccess, ::onFailed)
            } else {
                progressIndicator()
            }
        }

    }

    private fun progressIndicator() {
        binding.signUpProgresss.isVisible = !binding.signUpProgresss.isVisible
    }

    private fun onFailed() {
        progressIndicator()
        Toast.makeText(requireContext(), "Failed To Create Account!", Toast.LENGTH_SHORT).show()
//        lifecycleScope.launch {
//            delay(2000L)
//            withContext(Dispatchers.Main) {
//                navigateToMain()
//            }
//        }
    }

    private fun onSuccess() {
        progressIndicator()
        Toast.makeText(requireContext(), "Account Created", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            delay(2000L)
            withContext(Dispatchers.Main) {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun validate(): Boolean {
        if (!binding.checkBoxAgree.isChecked) {
            Toast.makeText(requireContext(), "Please Agree to proceed", Toast.LENGTH_SHORT).show()
            return false
        } else  if (binding.inputPhoneNumber.editText!!.text.length < 10) {
            Toast.makeText(requireContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show()
            return false
        } else  if (binding.inputPassword.editText!!.text.length < 6) {
            Toast.makeText(requireContext(), "Password need to be at least 6 digit", Toast.LENGTH_SHORT).show()
            return false
        } else if (!(binding.genderMale.isChecked || binding.genderFemale.isChecked)) {
            Toast.makeText(requireContext(), "Select gender", Toast.LENGTH_SHORT).show()
            return false
        } else if (!binding.btnSelectDate.text.contains(
                "/")) {
            Toast.makeText(requireContext(), "Select Date of birth", Toast.LENGTH_SHORT).show()
        }

        return binding.checkBoxAgree.isChecked && binding.inputFullName.editText!!.text.isNotEmpty() && binding.inputEmail.editText!!.text.toString()
            .isNotEmpty() &&
                binding.inputPhoneNumber.editText!!.text.isNotEmpty() && binding.inputPassword.editText!!.text.isNotEmpty() &&
                (binding.genderMale.isChecked or binding.genderFemale.isChecked) && binding.btnSelectDate.text.contains(
            "/"
        )
    }

    private fun signupInfo(): SignUpInfo {
        val info = SignUpInfo(
            binding.inputFullName.editText!!.text.toString(),
            binding.inputEmail.editText!!.text.toString(),
            binding.inputPhoneNumber.editText!!.text.toString(),
            binding.inputPassword.editText!!.text.toString(),
            if (binding.genderMale.isChecked) {
                binding.genderMale.text.toString()
            } else {
                binding.genderMale.text.toString()
            },
            binding.btnSelectDate.text.toString()
        )

        return info
    }

    private fun instantiateDatePicker() {
        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date Of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = formatDate(it)
            Log.d("Bmh", "instantiateDatePicker: it::$it, selection::${selectedDate}")
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

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.signUpLayout.id -> hideKeyboard(requireActivity(), requireView().findFocus())
        }
    }
}