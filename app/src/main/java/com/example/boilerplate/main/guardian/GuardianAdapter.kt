package com.example.boilerplate.main.guardian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.boilerplate.databinding.RowGuardianBinding
import com.example.boilerplate.main.guardian.model.Guardian

class GuardianAdapter(
    private val guardian: MutableList<Guardian>,
    private val viewModel: GuardianViewModel,
    private val deleteItem: (Int) -> Unit
) :
    RecyclerView.Adapter<GuardianAdapter.ViewHolder>() {

    class ViewHolder(val binding: RowGuardianBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowGuardianBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return guardian.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = guardian[position]

        holder.binding.guardianName.text = item.name
        holder.binding.guardianRelation.text = item.relation

        holder.binding.btnDeleteGuardian.isVisible = viewModel.isDelete

        holder.binding.btnDeleteGuardian.setOnClickListener {
            deleteItem(position)
        }
    }

}
