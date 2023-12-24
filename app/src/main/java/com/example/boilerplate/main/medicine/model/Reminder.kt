package com.example.boilerplate.main.medicine.model

data class Reminder(
    val medicineName: String,
    val doseAmount: String,
    val time: String,
    val repeatedAlarm: String,
    val reminder: String,
    val notification_id: String
)
