package com.saqtan.saqtan.di

import android.app.NotificationChannel
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.saqtan.saqtan.data.model.notification.AlarmScheduler
import com.saqtan.saqtan.data.model.notification.AndroidAlarmScheduler
import com.saqtan.saqtan.data.model.notification.NotificationHelper
import com.saqtan.saqtan.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** модуль внедрения зависимостей
 * */
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
        database:FirebaseDatabase,
        notificationHelper: NotificationHelper,
        alarmScheduler: AlarmScheduler
    ): UserRepository = UserRepository(auth,firestore,context,database,notificationHelper,alarmScheduler)

    @Singleton
    @Provides
    fun provideScheduler(
        @ApplicationContext
        context: Context,
    ):AlarmScheduler = AndroidAlarmScheduler(context)

    @Provides
    fun provideNotificationHelper(
        @ApplicationContext
        context: Context
    ):NotificationHelper = NotificationHelper(context)
    @Provides
    fun provideNotificationChannel(
        @ApplicationContext
        context: Context,
    ):NotificationChannel = NotificationHelper(context).getNotificationChannel()
}