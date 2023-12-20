package com.example.boilerplate.login

import com.example.boilerplate.model.RegistrationModel

class RegisterManager {

    /**
     * initiateRegistration
     * :validate form
     * :submit registration
     */
    fun initiateRegistration(userInfo: RegistrationModel, onSuccess: () -> Unit, onFailed: () -> Unit) {

        if (validateForm(userInfo)) {

        } else {
            onFailed.invoke()
        }
    }

    /**
     * Validate form is filled
     */
    private fun validateForm(userInfo: RegistrationModel): Boolean {
        return false
    }

    /**
     * Submit Registration
     * return:
     * success -> onSuccess()
     * failed -> onFailed()
     */
    private fun registerUser(userInfo: RegistrationModel, onSuccess: () -> Unit, onFailed: () -> Unit) {

    }
}