package com.example.boilerplate.model

data class RegistrationModel(
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val gender: String,
    val dateOfBirth: String,
    val consent: Boolean
)
