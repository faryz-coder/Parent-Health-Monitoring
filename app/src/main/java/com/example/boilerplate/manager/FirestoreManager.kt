package com.example.boilerplate.manager

import com.example.boilerplate.model.UserInfo
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreManager {
    private val db = Firebase.firestore

    fun getUserInfo(email: String, setUserInfo: (UserInfo) -> Unit) {
        val docRef = db.collection("user").document(email)
        var userInfo: UserInfo
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userInfo = UserInfo(
                        document.getField<String>("fullName").toString(),
                        document.getField<String>("email").toString(),
                        document.getField<String>("phoneNumber").toString(),
                        document.getField<String>("gender").toString(),
                        document.getField<String>("dateOfBirth").toString(),
                        document.getField<String>("height") ?: "0",
                        document.getField<String>("weight") ?: "0",
                        document.getField<String>("userImage") ?: "",
                    )
                    setUserInfo(userInfo)
                }
            }
    }
}