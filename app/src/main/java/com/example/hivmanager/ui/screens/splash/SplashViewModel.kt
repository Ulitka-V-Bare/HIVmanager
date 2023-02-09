package com.example.hivmanager.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val auth: FirebaseAuth
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
            delay(1000)
            //auth.signOut()
            try{
                auth.currentUser?.reload()
            }catch (e:FirebaseAuthInvalidUserException){
                auth.signOut()
            }
                //load data into UserRepository
            if(auth.currentUser==null){
                _navigationEvent.send(NavigationEvent.Navigate(Route.signIn,true))
            }
            else{
                _navigationEvent.send(NavigationEvent.Navigate(Route.addPill,true))
            }
        }
    }
}