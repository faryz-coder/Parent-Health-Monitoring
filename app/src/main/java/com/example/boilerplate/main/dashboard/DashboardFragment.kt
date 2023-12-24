package com.example.boilerplate.main.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentDashboardBinding
import com.example.boilerplate.main.MainViewModel
import com.example.boilerplate.model.UserInfo
import com.squareup.picasso.Picasso
import java.util.Locale

class DashboardFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var mainViewModel: MainViewModel

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.btnAddMedicine.setOnClickListener(this)
        binding.imageProfile.setOnClickListener(this)
        binding.foodTracker.setOnClickListener(this)
        binding.listGuardian.setOnClickListener(this)
        binding.calculateBmi.setOnClickListener(this)

        mainViewModel.userInfo.observe(viewLifecycleOwner) {
            Log.d("MainViewModel", "userInfo: $it")
            displayInfo(it)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnAddMedicine.id -> findNavController().navigate(R.id.action_dashboardFragment_to_listMedicineFragment)
            binding.imageProfile.id -> findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
            binding.foodTracker.id -> findNavController().navigate(R.id.action_dashboardFragment_to_foodTrackerFragment)
            binding.listGuardian.id -> findNavController().navigate(R.id.action_dashboardFragment_to_guardianFragment)
            binding.calculateBmi.id -> findNavController().navigate(R.id.action_dashboardFragment_to_bmiCalculatorFragment)
        }
    }

    private fun displayInfo(userInfo: UserInfo) {
        binding.viewWelcome.text = getString(R.string.hi_user, userInfo.fullName.capitalize())
        binding.viewHeight.text = getString(R.string.input_cm, userInfo.height)
        binding.viewWeight.text = getString(R.string.input_kg, userInfo.weight)

        if (userInfo.userImage.isNotEmpty()) {
            Picasso.get().load(userInfo.userImage).into(binding.imageViewProfile)
        }
    }
}