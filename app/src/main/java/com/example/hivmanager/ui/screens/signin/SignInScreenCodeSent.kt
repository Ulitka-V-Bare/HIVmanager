package com.example.hivmanager.ui.screens.signin

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
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.hivmanager.R
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.SignInButton
import com.example.hivmanager.ui.screens.components.SignInTextField
import com.example.hivmanager.ui.theme.HIVmanagerTheme
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
                append("Мы отправили код на номер ")
                withStyle(
                    style = SpanStyle(
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
            textAlign = TextAlign.Center
        )

        SignInTextField(
            value = codeFieldValue,
            onValueChange = onCodeFieldValueChange,
            label = { Text(text = "Код подтверждения") },
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
                text = "Войти"
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
                    "Отправить снова"
                else {
                    when (timerSeconds) {
                        60 -> "Отправить снова(1:00)"
                        in 10..59 -> "Отправить снова(0:${timerSeconds})"
                        in 1..9 -> "Отправить снова(0:0${timerSeconds})"
                        else -> "Отправить снова"
                    }

                }
            Text(
                modifier = Modifier
                    .clickable(enabled = timerSeconds == 0) { onResendCodeButtonClick() },
                text = text,
                textDecoration = TextDecoration.Underline,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = " ИЛИ ",
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .clickable { onChangePhoneNumberClick() },
                text = "Ввести другой номер",
                textDecoration = TextDecoration.Underline,
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