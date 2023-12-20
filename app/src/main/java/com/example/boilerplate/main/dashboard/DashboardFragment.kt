package com.example.boilerplate.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.btnAddMedicine.setOnClickListener(this)
        binding.imageProfile.setOnClickListener(this)
        binding.foodTracker.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnAddMedicine.id -> findNavController().navigate(R.id.action_dashboardFragment_to_medicineFragment2)
            binding.imageProfile.id -> findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
            binding.foodTracker.id -> findNavController().navigate(R.id.action_dashboardFragment_to_foodTrackerFragment)
        }
    }
}