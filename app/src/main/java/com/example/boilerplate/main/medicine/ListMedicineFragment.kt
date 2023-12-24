package com.example.boilerplate.main.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boilerplate.R
import com.example.boilerplate.databinding.FragmentListMedicineBinding
import com.example.boilerplate.main.medicine.model.Reminder
import com.example.boilerplate.manager.SharedPreferencesManager

class ListMedicineFragment : Fragment() {
    private var _binding: FragmentListMedicineBinding? = null

    private val binding get() = _binding!!

    private val listReminder = mutableListOf<Reminder>()

    private lateinit var medicineAdapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMedicineBinding.inflate(inflater, container, false)

        listReminder.addAll(SharedPreferencesManager(requireActivity()).getReminder())

        binding.listReminderRecyclerView.apply {
            medicineAdapter = MedicineAdapter(listReminder)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = medicineAdapter
        }

        binding.addMedicine.setOnClickListener {
            findNavController().navigate(R.id.medicineFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listReminder.clear()
        listReminder.addAll(SharedPreferencesManager(requireActivity()).getReminder())
        medicineAdapter.notifyDataSetChanged()
    }
}