package com.example.boilerplate.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boilerplate.manager.AuthManager
import com.example.boilerplate.manager.FirestoreManager
import com.example.boilerplate.model.UserInfo

class MainViewModel: ViewModel() {

    init {
        FirestoreManager().getUserInfo(AuthManager().userEmail(), ::setUserInfo)
    }

    private val _email = MutableLiveData<String>().apply {
        value = AuthManager().userEmail()
    }

    private val _userInfo = MutableLiveData<UserInfo>()

    val email = _email
    val userInfo = _userInfo

    private fun setUserInfo(info: UserInfo) {
        userInfo.value = info
    }

}