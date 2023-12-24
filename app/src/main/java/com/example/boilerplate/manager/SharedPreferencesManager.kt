package com.example.boilerplate.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.boilerplate.R
import com.example.boilerplate.main.medicine.model.Reminder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferencesManager(private val activity: Activity) {

    private val sharedPref = activity.getSharedPreferences(
        activity.getString(R.string.app_name), Context.MODE_PRIVATE
    ) ?: null

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

    fun addReminder(newReminder: Reminder) {
        if (sharedPref != null) {
            val listReminder = sharedPref.getString(activity.getString(R.string.reminder), "")
            if (listReminder != null) {
                if (listReminder.isEmpty()) {
                    val list = mutableListOf<Reminder>()
                    list.add(newReminder)

                    with(sharedPref.edit()) {
                        this.putString(activity.getString(R.string.reminder), listToJsonString(list))
                        this.apply()
                    }

                } else {

                    Log.d("SharedPreferencesManager", "content: $listReminder")
                    val list = jsonStringToList(listReminder)
                    list.add(newReminder)
                    with(sharedPref.edit()) {
                        this.putString(activity.getString(R.string.reminder), listToJsonString(list))
                        this.apply()
                    }

                }
            }
        }
    }

    fun getReminder(): List<Reminder> {
        if (sharedPref != null) {
            val listReminder = sharedPref.getString(activity.getString(R.string.reminder), "")
            return if (listReminder.isNullOrEmpty()) {
                emptyList()
            } else {
                jsonStringToList(listReminder)
            }
        }
        return emptyList()
    }

    private fun listToJsonString(list: List<Reminder>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    private fun jsonStringToList(jsonString: String): MutableList<Reminder> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Reminder>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}