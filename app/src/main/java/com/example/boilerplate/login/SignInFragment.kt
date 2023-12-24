package com.example.boilerplate.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentSigninBinding
import com.example.boilerplate.login.model.SignInInfo
import com.example.boilerplate.main.MainActivity
import com.example.boilerplate.manager.AuthManager
import com.example.boilerplate.manager.SharedPreferencesManager
import com.example.boilerplate.utils.UtilsInterface
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignInFragment : Fragment(), View.OnClickListener, UtilsInterface {

    private var _binding: FragmentSigninBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener(this)
        binding.btnRegisterNewUser.setOnClickListener(this)
        binding.signInLayout.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun progressIndicator() {
        binding.signInProgress.isVisible = !binding.signInProgress.isVisible
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnSignIn.id -> login()
            binding.btnRegisterNewUser.id -> navigateToRegister()
            binding.signInLayout.id -> hideKeyboard(requireActivity(), requireView().findFocus())
        }
    }

    override fun onResume() {
        super.onResume()
        AuthManager().isUserSignIn().let {
            if (it) {
                navigateToMain()
            } else {
                val rememberMe = SharedPreferencesManager(requireActivity()).getRememberMe().let { me ->
                    if (me.isNotEmpty()) {
                        binding.checkBoxRemember.isChecked = true
                        binding.signinUsername.editText?.setText(me.split(",")[0])
                        binding.signinPassword.editText?.setText(me.split(",")[1])
                    }
                }
                Log.d("SignInFragment", "rememberMe: $rememberMe")
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    /**
     * Try to Sign In user
     */
    private fun login() {
        progressIndicator()

        if (binding.checkBoxRemember.isChecked) {
            rememberMe()
        } else {
            SharedPreferencesManager(requireActivity()).cancelRememberMe()
        }

        if (binding.signinUsername.editText?.text.toString().isNotEmpty() && binding.signinPassword.editText?.text.toString().isNotEmpty()) {
            AuthManager().login(signInInfo(), ::onSuccess, ::onFailed)

        }

    }

    private fun signInInfo(): SignInInfo {
        val info = SignInInfo (
            binding.signinUsername.editText?.text.toString(),
            binding.signinPassword.editText?.text.toString()
        )

        return info
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

        SharedPreferencesManager(requireActivity()).setRememberMe(
            binding.signinUsername.editText?.text.toString(),
            binding.signinPassword.editText?.text.toString()
        )
    }

    private fun onSuccess() {
        progressIndicator()
        navigateToMain()
    }

    private fun onFailed() {
        progressIndicator()
        Snackbar.make(requireView(), "Login Failed", Snackbar.LENGTH_LONG).show()
    }
}