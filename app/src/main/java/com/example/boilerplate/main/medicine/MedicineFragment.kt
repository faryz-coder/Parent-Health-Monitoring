package com.example.boilerplate.main.medicine

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boilerplate.R
import com.example.boilerplate.broadcast.AlarmReceiver
import com.example.boilerplate.databinding.FragmentMedicineBinding
import com.example.boilerplate.main.medicine.model.Reminder
import com.example.boilerplate.manager.SharedPreferencesManager
import com.example.boilerplate.utils.UtilsInterface
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

class MedicineFragment : Fragment(), View.OnClickListener, UtilsInterface {
    private var _binding: FragmentMedicineBinding? = null

    private val binding get() = _binding!!
    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)

        binding.submitReminder.setOnClickListener(this)
        binding.btnSelectTime.setOnClickListener(this)
        binding.medicineLayout.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnSelectTime.id -> selectTime().show(childFragmentManager, "Select Time")
            binding.submitReminder.id -> initiateReminder()
            binding.medicineLayout.id -> hideKeyboard(requireActivity(), requireView().findFocus())
        }
    }

    private fun initiateReminder() {
        if (validate()) {
            val total = SharedPreferencesManager(requireActivity()).getReminder()

            var repeated = if (binding.radioYes.isChecked) {
                "daily"
            } else {
                "today"
            }
            val reminder = Reminder(
                binding.inputMedicineName.editText?.text.toString(),
                binding.inputDoseAmount.editText?.text.toString(),
                selectedTime,
                repeated,
                binding.inputReminders.editText?.text.toString(),
                total.size.toString()
            )

            Log.d("MedicineFragment", "total : ${total.size}")
            createReminder(reminder)
            findNavController().popBackStack()
//
        }
    }

    private fun validate(): Boolean {
        val valid = binding.inputMedicineName.editText?.text.toString()
            .isNotEmpty() && binding.inputDoseAmount.editText?.text.toString().isNotEmpty()
                && selectedTime.isNotEmpty() && binding.radioYes.isChecked || binding.radioNo.isChecked && binding.inputReminders.editText?.text.toString()
            .isNotEmpty()

        if (!valid) {
            Toast.makeText(requireContext(), "Form Incomplete", Toast.LENGTH_LONG).show()
        }

        return valid
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun createReminder(reminder: Reminder) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)

        alarmIntent.putExtra("notification_id", reminder.notification_id.toInt())
        alarmIntent.putExtra("notification_message", "Reminder to take ${reminder.doseAmount}x of ${reminder.medicineName} special note: ${reminder.reminder}")

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity(), reminder.notification_id.toInt(), alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, selectedTime.split(":")[0].toInt())
        calendar.set(Calendar.MINUTE,  selectedTime.split(":")[1].toInt())

        SharedPreferencesManager(requireActivity()).addReminder(reminder)

        if (reminder.repeatedAlarm == "daily") {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            ).let {
                Log.d("MedicineFragment", "Alarm Set Daily")
            }
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            ).let {
                Log.d("MedicineFragment", "Alarm Set Today")
            }
        }
    }

    private fun selectTime(): MaterialTimePicker {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        picker.addOnPositiveButtonClickListener {
            Log.d("MedicineFragment", "minute: ${picker.minute}, hour: ${picker.hour}")
            selectedTime = "${picker.hour}:${picker.minute}"

            binding.btnSelectTime.text = convert24to12(selectedTime)
        }

        return picker
    }

    private fun convert24to12(hour24: String): String {
        val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val date = sdf24.parse(hour24)
        return sdf12.format(date!!)
    }
}