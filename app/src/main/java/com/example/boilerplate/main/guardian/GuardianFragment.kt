package com.example.boilerplate.main.guardian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boilerplate.databinding.DialogAddGuardianBinding
import com.example.boilerplate.databinding.FragmentGuardianBinding
import com.example.boilerplate.main.guardian.model.Guardian
import com.example.boilerplate.manager.FirestoreManager
import com.example.boilerplate.utils.UtilsInterface
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class GuardianFragment : Fragment(), View.OnClickListener, UtilsInterface {
    private var _binding: FragmentGuardianBinding? = null
    private lateinit var viewModel: GuardianViewModel

    private val binding get() = _binding!!
    private val guardian = mutableListOf<Guardian>()
    private lateinit var guardianAdapter: GuardianAdapter
    private val isDelete = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuardianBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GuardianViewModel::class.java]

        binding.listGuardianRecyclerView.apply {
            guardianAdapter = GuardianAdapter(guardian, viewModel, ::deleteItem)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = guardianAdapter
        }

        viewModel.listGuardian.observe(viewLifecycleOwner) {
            guardian.clear()
            guardian.addAll(it)
            guardianAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addGuardian.setOnClickListener {
            if (guardian.size < 5) {
                dialogAddGuardian().show()
            } else {
                Snackbar.make(requireView(), "Guard already MAX", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.deleteGuardian.setOnClickListener(this)
    }

    private fun dialogAddGuardian(): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val addGuardianDialog = DialogAddGuardianBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(addGuardianDialog.root)

        addGuardianDialog.btnAddGuardian.setOnClickListener {
            if(addGuardianDialog.inputGuardianName.editText!!.text.isNotEmpty() &&
                addGuardianDialog.inputGuardianRelation.editText!!.text.isNotEmpty()) {
                guardian.add(
                    Guardian(
                        addGuardianDialog.inputGuardianName.editText!!.text.toString(),
                        addGuardianDialog.inputGuardianRelation.editText!!.text.toString(),
                        ""
                    )
                ).let {
                    guardianAdapter.notifyItemInserted(guardian.size)
                    FirestoreManager().addGuardian(guardian.last(), {viewModel.refresh()})
                }
            }
            bottomSheetDialog.dismiss()
        }
        return bottomSheetDialog
    }

    /**
     * Delete guardian
     */
    private fun deleteItem(index: Int) {
        FirestoreManager().removeGuardian(guardian[index].id, {viewModel.refresh()}).let {
            guardian.removeAt(index)
            guardianAdapter.notifyItemRemoved(index)
        }
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.deleteGuardian.id -> {
                viewModel.isDelete = !viewModel.isDelete
                guardianAdapter.notifyDataSetChanged()
            }
        }
    }
}