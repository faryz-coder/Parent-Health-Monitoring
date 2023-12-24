package com.example.boilerplate.manager

import android.util.Log
import com.example.boilerplate.login.model.SignInInfo
import com.example.boilerplate.login.model.SignUpInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthManager {

    private var auth: FirebaseAuth = Firebase.auth

    init {

    }

    fun isUserSignIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun userEmail(): String {
        return auth.currentUser!!.email.toString()
    }

    fun signOut() {
        auth.signOut()
    }

    fun login(signInInfo: SignInInfo, onSuccess: () -> Unit, onFailed: () -> Unit) {
        auth.signInWithEmailAndPassword(signInInfo.email, signInInfo.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }
    }

    fun createUser(signUpInfo: SignUpInfo, onSuccess: () -> Unit, onFailed: () -> Unit) {
        auth.createUserWithEmailAndPassword(signUpInfo.email, signUpInfo.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Sign in success
                    addUserInfo(signUpInfo)
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }
    }

    private fun addUserInfo(signUpInfo: SignUpInfo) {
        val db = Firebase.firestore

        val data = hashMapOf(
            "fullName" to signUpInfo.fullName,
            "email" to signUpInfo.email,
            "phoneNumber" to signUpInfo.phoneNumber,
            "gender" to signUpInfo.gender,
            "dateOfBirth" to signUpInfo.dateOfBirth
        )

        db.collection("user").document(signUpInfo.email)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d("AuthManager", "createUser->addUserInfo:success") }
            .addOnFailureListener { Log.w("AuthManager", "createUser->addUserInfo:failed") }
    }


}