package com.example.boilerplate.main.medicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boilerplate.databinding.RowMedicineBinding
import com.example.boilerplate.main.medicine.model.Reminder

class MedicineAdapter(private val listReminder: MutableList<Reminder>) :
    RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {
    class ViewHolder(val binding: RowMedicineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listReminder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listReminder[position]

        holder.binding.rowNo.text = buildString {
            append(position + 1)
        }

        holder.binding.rowMedicine.text = buildString {
            append("Medicine: ")
            append(item.medicineName)
        }
        holder.binding.rowDose.text = buildString {
            append("Dose: ")
            append(item.doseAmount)
        }
        holder.binding.rowTime.text = buildString {
            append("Time: ")
            append(item.time)
        }
        holder.binding.rowReminder.text = buildString {
            append("Reminder: ")
            append(item.reminder)
        }
        holder.binding.rowRepeat.text = buildString {
            append("Repeat: ")
            append(item.repeatedAlarm)
        }
    }

}
