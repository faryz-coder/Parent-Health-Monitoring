package com.example.boilerplate.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentSigninBinding
import com.example.boilerplate.main.dashboard.MainActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignInFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSigninBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener(this)
        binding.btnRegisterNewUser.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnSignIn.id -> login()
            binding.btnRegisterNewUser.id -> navigateToRegister()
        }
    }

    /**
     * Try to Sign In user
     */
    private fun login() {
        if (binding.checkBoxRemember.isChecked) {
            rememberMe()
        }

        // Proceeed to login
        // Call Login Manager to handle login
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    /***
     * Navigate to registration screen
     */
    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
    }

    /***
     * Stored Email into sharedPreference
     */
    private fun rememberMe() {

    }

    private fun onSuccess() {

    }

    private fun onFailed() {

    }
}