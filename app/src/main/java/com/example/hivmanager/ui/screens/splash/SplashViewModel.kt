package com.example.hivmanager.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository
) :ViewModel(){

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.OnSplashScreenLaunched -> onSplashScreenLaunched()
        }
    }
    private fun onSplashScreenLaunched(){
        viewModelScope.launch{
            delay(500)
          //  auth.signOut()


            try{
                auth.currentUser?.reload()
            }catch (e:FirebaseAuthInvalidUserException){
                auth.signOut()
            }
            Log.d("splash","before loadUserLocalData")
           // userRepository.loadUserLocalData()
            userRepository.updateUserDataOnDatabase()
            Log.d("splash","after loadUserLocalData")
                //load data into UserRepository
            if(auth.currentUser==null){
                _navigationEvent.send(NavigationEvent.Navigate(Route.signIn,))
            }
            else{
                Log.d("splash","before")
                userRepository.loadUserData(auth.uid!!)
                Log.d("splash","after")
                if(userRepository.userType=="user"){
                    _navigationEvent.send(NavigationEvent.Navigate(Route.home,))
                }
                if(userRepository.userType=="doctor"){
                    _navigationEvent.send(NavigationEvent.Navigate(Route.doctorHome))
                }
            }
        }
    }
}