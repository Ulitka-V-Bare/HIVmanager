package com.example.hivmanager.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.example.hivmanager.data.model.*
import com.example.hivmanager.ui.screens.chat.Message
import com.google.firebase.auth.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore by dataStore("data.json", UserDataSerializer)
@Singleton
class UserRepository @Inject constructor(
    private val auth:FirebaseAuth,
    private val firestore:FirebaseFirestore,
    @ApplicationContext
    val context: Context,
    private val database:FirebaseDatabase
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
    fun addUserToDatabase(uid:String?){
        var ifExists = false
        if(uid==null) return
        firestore.collection(Constants.USERS).document(uid).get().addOnCompleteListener() {task->
            if(task.result.exists()) ifExists=true
            if(!ifExists) {
                firestore.collection(Constants.USERS).document(uid).set(
                    mapOf("type" to "user"),
                    SetOptions.merge()
                )
            }
        }
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

    fun sendMessage(message:String){
        val ref = database.getReference("messages").child("testmessages").child("${com.google.firebase.Timestamp.now()}")
        ref.updateChildren(mapOf(
            "message" to message,
            "time" to "${com.google.firebase.Timestamp.now().seconds}",
            "author" to "${auth.uid}"
        ))
    }

    fun setOnUpdateListener(onChildAddedListener: (DataSnapshot)->Unit,onLoaded:()->Unit){
        val ref = database.getReference("messages").child("testmessages").addChildEventListener(
            object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    onChildAddedListener(snapshot)
                    onLoaded()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

    }

    suspend fun getMessageList(chatID:String,onGetData:(MutableList<Message>)->Unit){
        val messageList:MutableList<Message> = mutableListOf()
        val ref = database.getReference("messages").child("testmessages").addListenerForSingleValueEvent(
            object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(child in snapshot.children){
                        val iterator = child.children.iterator()
                        while (iterator.hasNext()){
                            val senderID = iterator.next().value
                            val message = iterator.next().value
                            val time = iterator.next().value
                            messageList.add(
                                Message(
                                    senderID.toString(),
                                    message.toString(),
                                    time.toString().toLong()
                                )
                            )
                        }
                    }
                    onGetData(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}