package com.example.hivmanager.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth:FirebaseAuth,
    private val firestore:FirebaseFirestore,
){
    fun sendVerificationCode(
        phoneNumber:String,
        callbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    ){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



}