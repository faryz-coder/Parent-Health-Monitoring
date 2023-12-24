package com.example.boilerplate.manager

import android.net.Uri
import com.example.boilerplate.model.UserInfo
import com.google.firebase.firestore.SetOptions
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
                        document.getField<String>("address") ?: "",
                        document.getField<String>("about") ?: "",

                        )
                    setUserInfo(userInfo)
                }
            }
    }

    fun updateUserInfo(imageUri: Uri?, userInfo: UserInfo, onSuccess: () -> Unit) {
        if (imageUri != null) {
            //Upload Image first
            StorageManager().uploadImg(imageUri, userInfo, ::updateUser, onSuccess)
        } else {
            updateUser(userInfo, onSuccess)
        }
    }

    private fun updateUser(userInfo: UserInfo, onSuccess: () -> Unit) {
        val data = hashMapOf(
            "fullName" to userInfo.fullName,
            "phoneNumber" to userInfo.phoneNumber,
            "address" to userInfo.address,
            "about" to userInfo.about,

        )
        if (userInfo.userImage.isNotEmpty()) {
            data["userImage"] = userInfo.userImage
        }

        db.collection("user").document(AuthManager().userEmail())
            .set(data, SetOptions.merge())
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener {  }
    }
}