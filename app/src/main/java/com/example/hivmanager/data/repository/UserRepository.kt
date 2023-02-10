package com.example.hivmanager.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.model.PillInfo_example
import com.example.hivmanager.data.model.UserData
import com.example.hivmanager.data.model.UserDataSerializer
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.A
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore by dataStore("data.json", UserDataSerializer)
@Singleton
class UserRepository @Inject constructor(
    private val auth:FirebaseAuth,
    private val firestore:FirebaseFirestore,
    @ApplicationContext
    val context: Context
){
    var userData: UserData = UserData()
    fun loadPillInfoList(scope:CoroutineScope){
        scope.launch {
            userData = context.dataStore.data.first()
        }
    }

    init {
        Log.d("repo","initialization")
    }

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

    fun getUserPills(): List<PillInfo> {
        return userData.pillInfoList
    }

    suspend fun updatePillsList(pillsList: List<PillInfo>){
        context.dataStore.updateData {
            it.copy(pillInfoList =pillsList.toPersistentList())
        }
    }

    suspend fun deletePillInfo(index:Int){
        context.dataStore.updateData {
            it.copy(pillInfoList = it.pillInfoList.minus(it.pillInfoList[index]))
        }
        userData = userData.copy(pillInfoList = userData.pillInfoList.minus(userData.pillInfoList[index]))
    }

    suspend fun addPillInfo(pillInfo: PillInfo){
        context.dataStore.updateData {
            it.copy(pillInfoList = it.pillInfoList.plus(pillInfo))
        }
        userData = userData.copy(pillInfoList = userData.pillInfoList.plus(pillInfo))
        Log.d("repo","$pillInfo")
    }
}