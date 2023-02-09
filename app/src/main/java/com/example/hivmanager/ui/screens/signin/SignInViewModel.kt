package com.example.hivmanager.ui.screens.signin

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.example.hivmanager.ui.screens.splash.SplashEvent
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    @ApplicationContext
    val context: Context
) : ViewModel(){

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    var state by mutableStateOf(SignInState())
        private set

    var storedVerificationId:String = ""

    private val timer = object : CountDownTimer(60000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            state=state.copy(
                timerSeconds = state.timerSeconds - 1
            )
        }
        override fun onFinish() {
            state=state.copy(
                timerSeconds = 0
            )
        }
    }

    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.OnSignInButtonClick -> onSignInButtonClick()
            SignInEvent.OnResendCodeButtonClick ->{}
            is SignInEvent.OnSendCodeButtonClick -> onSendCodeButtonClick(event.activity)
            is SignInEvent.OnCodeChange -> onCodeChanged(event.code)
            is SignInEvent.OnPhoneNumberChange -> onPhoneNumberChanged(event.phoneNumber)
            SignInEvent.OnChangePhoneNumberClick ->onChangePhoneNumberClick()
        }
    }

    private fun onCodeChanged(code:String){
        state = state.copy(
            code=code
        )
    }
    private fun onPhoneNumberChanged(phoneNumber:String){
        state = state.copy(
            phoneNumber = phoneNumber
        )
    }

    private fun onChangePhoneNumberClick(){
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateUp)
        }
    }

    private fun onSignInButtonClick(){
        val credential = PhoneAuthProvider.getCredential(storedVerificationId,state.code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun onSendCodeButtonClick(activity: Activity){
        var resendToken: PhoneAuthProvider.ForceResendingToken? = null
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
                //navigate to home screen
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(context,"Please, check entered phone number",Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(context,"The SMS quota for the project has been exceeded",Toast.LENGTH_SHORT).show()
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                Toast.makeText(context,"Code successfully sent", Toast.LENGTH_SHORT).show()
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                state = state.copy(
                    timerSeconds = 60
                )
                timer.start()
                viewModelScope.launch {
                    _navigationEvent.send(NavigationEvent.Navigate(Route.signInCodeSent))
                }
                //Log.d(TAG, "Signed in")
            }
        }

        //userRepository.sendVerificationCode(state.phoneNumber,callbacks)
        userRepository.sendVerificationCode("+79831736430",callbacks,activity)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    //navigate to home screen
                    viewModelScope.launch {
                        _navigationEvent.send(NavigationEvent.Navigate(Route.home))
                    }
                    Toast.makeText(context,"Successfully signed in",Toast.LENGTH_SHORT).show()
                    //val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        state = state.copy(
                            code = "",
                            isCodeError = true
                        )
                        Toast.makeText(context,"The verification code entered was invalid",Toast.LENGTH_SHORT).show()
                    } else{
                      //check internet toast
                    }
                    // Update UI
                }
            }
    }




}

private val TAG = "SignInViewModel"