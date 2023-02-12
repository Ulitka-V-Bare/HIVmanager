package com.example.hivmanager.di

import android.content.Context
import com.example.hivmanager.data.model.notification.AlarmReceiver
import com.example.hivmanager.data.model.notification.AlarmScheduler
import com.example.hivmanager.data.model.notification.AndroidAlarmScheduler
import com.example.hivmanager.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideFirebaseDatabase() = Firebase.database

    @Singleton
    @Provides
    fun provideUserRepository(
        auth:FirebaseAuth,
        firestore: FirebaseFirestore,
        @ApplicationContext
        context: Context,
        database:FirebaseDatabase
    ):UserRepository = UserRepository(auth,firestore,context,database)

    @Singleton
    @Provides
    fun provideScheduler(
        @ApplicationContext
        context: Context,
    ):AlarmScheduler = AndroidAlarmScheduler(context)

}