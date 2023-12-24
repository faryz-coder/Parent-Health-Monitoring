package com.example.boilerplate.main.guardian

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boilerplate.main.guardian.model.Guardian
import com.example.boilerplate.manager.FirestoreManager

class GuardianViewModel : ViewModel() {
    var isDelete = false

    init {
        FirestoreManager().getListGuardian(::setListGuardian)
    }

    private val _listGuardian = MutableLiveData<MutableList<Guardian>>()

    val listGuardian = _listGuardian

    private fun setListGuardian(listGuardian: MutableList<Guardian>) {
        _listGuardian.value = listGuardian
    }

    fun refresh() {
        FirestoreManager().getListGuardian(::setListGuardian)
    }
}