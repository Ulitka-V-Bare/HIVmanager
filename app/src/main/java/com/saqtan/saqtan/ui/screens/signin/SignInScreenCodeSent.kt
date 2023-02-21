package com.saqtan.saqtan.ui.screens.signin

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.ui.screens.components.SignInButton
import com.saqtan.saqtan.ui.screens.components.SignInTextField
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme
import com.saqtan.saqtan.R

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

    val activity = LocalContext.current as Activity

    SignInScreenCodeSentUi(
        phoneFieldValue = viewModel.state.phoneNumber,
        onResendCodeButtonClick = {viewModel.onEvent(SignInEvent.OnResendCodeButtonClick(activity))},
        onSignInButtonClick = {viewModel.onEvent(SignInEvent.OnSignInButtonClick)},
        onChangePhoneNumberClick = {viewModel.onEvent(SignInEvent.OnChangePhoneNumberClick)},
        codeFieldValue = viewModel.state.code,
        onCodeFieldValueChange = {viewModel.onEvent(SignInEvent.OnCodeChange(it))},
        isCodeError = viewModel.state.isCodeError,
        timerSeconds = viewModel.state.timerSeconds
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
        .fillMaxSize().background(color = MaterialTheme.colors.background),
    ) {
        val (codeField, textField, button, resendCodeOrChangePhoneRow,image) = createRefs()
        Image (
            modifier = Modifier
                .width(300.dp)
                .height(350.dp)
                .constrainAs(image){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ,
            painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.logo_light else R.drawable.logo_no_background),
            contentDescription = "Big logo"
        )
        Text(
            text = buildAnnotatedString {
                append("Біз ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                    )
                ) {
                    append("$phoneFieldValue")
                }
                append(" осы номерге смс код жібердік")
            },
            color = if(isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .width(200.dp)
                .constrainAs(textField) {
                    bottom.linkTo(codeField.top, 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            textAlign = TextAlign.Center
        )

        SignInTextField(
            value = codeFieldValue,
            onValueChange = onCodeFieldValueChange,
            label = { Text(text = "Растау коды",color = if(isSystemInDarkTheme()) Color.White else Color.Black) },
            modifier = Modifier.constrainAs(codeField) {
                top.linkTo(parent.top, 70.dp)
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
                text = "Кіру",color = Color.White
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
                    "Кодты қайтадан жіберу"
                else {
                    when (timerSeconds) {
                        60 -> "Кодты қайтадан жіберу(1:00)"
                        in 10..59 -> "Кодты қайтадан жіберу(0:${timerSeconds})"
                        in 1..9 -> "Кодты қайтадан жіберу(0:0${timerSeconds})"
                        else -> "Кодты қайтадан жіберу"
                    }

                }
            Text(
                modifier = Modifier
                    .clickable(enabled = timerSeconds == 0) { onResendCodeButtonClick() },
                text = text,
                textDecoration = TextDecoration.Underline,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = " немесе ".uppercase(),
                color = if(isSystemInDarkTheme()) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .clickable { onChangePhoneNumberClick() },
                text = "Басқа номер енгізу",
                textDecoration = TextDecoration.Underline,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun SignInScreenCodeSentPreview(){
    HIVmanagerTheme {
        SignInScreenCodeSentUi()
    }
}