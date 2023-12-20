package com.example.boilerplate.main.guardian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.boilerplate.databinding.FragmentGuardianBinding

class GuardianFragment : Fragment() {
    private var _binding: FragmentGuardianBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuardianBinding.inflate(inflater, container, false)
        return binding.root
    }
}