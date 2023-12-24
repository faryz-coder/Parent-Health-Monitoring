package com.example.boilerplate.manager

import android.net.Uri
import com.example.boilerplate.model.UserInfo
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.reflect.KFunction2

class StorageManager {
    private val storage = Firebase.storage

    fun uploadImg(imageUri: Uri, userInfo: UserInfo, updateUser: KFunction2<UserInfo, () -> Unit, Unit>, onSuccess: () -> Unit) {
        val storageRef = storage.reference
        val profileRef = storageRef.child("profle/${imageUri.lastPathSegment}")

        val uploadTask = profileRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileRef.downloadUrl
        }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    userInfo.userImage = downloadUri.toString()
                    updateUser.invoke(userInfo, onSuccess)
                }
            }
            .addOnFailureListener {

            }
    }
}