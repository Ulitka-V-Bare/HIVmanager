package com.example.hivmanager.ui.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.SignInButton
import com.example.hivmanager.ui.screens.components.SignInTextField
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun SignInScreenCodeSent(
    viewModel:SignInViewModel = hiltViewModel(),
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    onNavigateUp: () -> Unit,
){
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                NavigationEvent.NavigateUp -> {onNavigateUp()}
                else -> {}
            }
        }
    }

    SignInScreenCodeSentUi(
        phoneFieldValue = viewModel.state.phoneNumber,
        onResendCodeButtonClick = {viewModel.onEvent(SignInEvent.OnResendCodeButtonClick)},
        onSignInButtonClick = {viewModel.onEvent(SignInEvent.OnSignInButtonClick)},
        onChangePhoneNumberClick = {viewModel.onEvent(SignInEvent.OnChangePhoneNumberClick)},
        codeFieldValue = viewModel.state.code,
        onCodeFieldValueChange = {viewModel.onEvent(SignInEvent.OnCodeChange(it))},
        isCodeError = viewModel.state.isCodeError
    )
}

@Composable
fun SignInScreenCodeSentUi(
    phoneFieldValue: String = "7999999999",
    codeFieldValue: String = "",
    onCodeFieldValueChange: (String)->Unit = {},
    onResendCodeButtonClick:()->Unit = {},
    onSignInButtonClick:()->Unit = {},
    onChangePhoneNumberClick:()->Unit={},
    timerSeconds:Int = 0,
    isCodeError:Boolean = false
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    Color(0xFF6097ff),
                    Color(0xFF136acb),
                    Color(0xFF004099),
                )
            )
        ),
    ) {
        val (codeField, textField, button, resendCodeOrChangePhoneRow) = createRefs()
        Text(
            text = buildAnnotatedString {
                append("We have sent a verification code to ")
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("$phoneFieldValue")
                }
            },
            modifier = Modifier
                .width(200.dp)
                .constrainAs(textField) {
                    bottom.linkTo(codeField.top, 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = Color.White,
            textAlign = TextAlign.Center
        )

        SignInTextField(
            value = codeFieldValue,
            onValueChange = onCodeFieldValueChange,
            label = { Text(text = "Code") },
            modifier = Modifier.constrainAs(codeField) {
                top.linkTo(parent.top, 25.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            isError = isCodeError
        )

        SignInButton(
            onClick = onSignInButtonClick,
            modifier = Modifier
                .width(170.dp)
                .height(40.dp)
                .constrainAs(button) {
                    top.linkTo(codeField.bottom, 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                text = "Sign In"
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(resendCodeOrChangePhoneRow) {
                    top.linkTo(button.bottom, 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val text =
                if (timerSeconds == 0)
                    "Resend code"
                else {
                    when (timerSeconds) {
                        60 -> "Resend code(1:00)"
                        in 10..59 -> "Resend code(0:${timerSeconds})"
                        in 1..9 -> "Resend code(0:0${timerSeconds})"
                        else -> "Resend code"
                    }

                }
            Text(
                modifier = Modifier
                    .clickable(enabled = timerSeconds == 0) { onResendCodeButtonClick() },
                text = text,
                textDecoration = TextDecoration.Underline,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = " OR ",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .clickable { onChangePhoneNumberClick() },
                text = "Change phone number",
                textDecoration = TextDecoration.Underline,
                color = Color.White
            )
        }
    }
}