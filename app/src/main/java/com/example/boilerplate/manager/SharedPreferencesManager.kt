package com.example.boilerplate.manager

import android.app.Activity
import android.content.Context
import com.example.boilerplate.R

class SharedPreferencesManager(private val activity: Activity) {

    private val sharedPref = activity.getSharedPreferences(
        activity.getString(R.string.app_name), Context.MODE_PRIVATE) ?: null

    fun getRememberMe(): String {

        return sharedPref?.getString(activity.getString(R.string.rememberMe), "") ?: ""
    }

    fun setRememberMe(email: String, password: String) {
        with(sharedPref?.edit()) {
            this?.putString(activity.getString(R.string.rememberMe), "$email,$password")
            this?.apply()
        }
    }

    fun cancelRememberMe() {
        if (sharedPref != null) {
            val saved = sharedPref.getString(activity.getString(R.string.rememberMe), "")
            if (saved != null) {
                if (saved.isNotEmpty()) {
                    sharedPref.edit()?.clear()
                        ?.apply()
                }
            }
        }
    }
}