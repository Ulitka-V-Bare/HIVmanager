package com.saqtan.saqtan.ui.screens.signin

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.saqtan.saqtan.R
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.ui.screens.components.SignInButton
import com.saqtan.saqtan.ui.screens.components.SignInTextField
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme

@Composable
fun SignInScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                else -> {}
            }
        }
    }
    SignInScreenUi(
        phoneFieldValue = viewModel.state.phoneNumber,
        onPhoneFieldValueChange = { viewModel.onEvent(SignInEvent.OnPhoneNumberChange(it)) },
        onSendCodeButtonClick = { viewModel.onEvent(SignInEvent.OnSendCodeButtonClick(activity)) },
        timerSeconds = viewModel.state.timerSeconds
    )
}

@Composable
private fun SignInScreenUi(
    phoneFieldValue: String = "7999999999",
    onPhoneFieldValueChange: (String) -> Unit = {},
    onSendCodeButtonClick: () -> Unit = {},
    timerSeconds: Int = 0,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            ),
    ) {

        val (phoneField, textField, button,logo) = createRefs()
        Image (
            modifier = Modifier
                .width(300.dp)
                .height(350.dp)
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            painter = painterResource(id = if(isSystemInDarkTheme()) R.drawable.logo_light else R.drawable.logo_no_background),
            contentDescription = "Big logo"
        )
        Text(
            text = "Өз нөміріңізді енгізіңіз, біз сізге СМС код жібереміз",
            modifier = Modifier
                .constrainAs(textField) {
                    bottom.linkTo(phoneField.top, 5.dp)
                    start.linkTo(parent.start, 40.dp)
                    end.linkTo(parent.end, 40.dp)
                }
                .width(230.dp),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
        SignInTextField(
            value = phoneFieldValue,
            onValueChange = onPhoneFieldValueChange,

            label = { Text(text = "71234567890",color = MaterialTheme.colors.primary) },
            modifier = Modifier.constrainAs(phoneField) {
                top.linkTo(parent.top, 70.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        SignInButton(
            onClick = onSendCodeButtonClick,
            modifier = Modifier
                .width(180.dp)
                .height(40.dp)
                .constrainAs(button) {
                    top.linkTo(phoneField.bottom, 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            enabled = timerSeconds == 0
        ) {
            val text =
                if (timerSeconds == 0)
                    "Кодты жіберу"
                else {
                    when (timerSeconds) {
                        60 -> "Кодты жіберу(1:00)"
                        in 10..59 -> "Кодты жіберу(0:${timerSeconds})"
                        in 1..9 -> "Кодты жіберу(0:0${timerSeconds})"
                        else -> "Кодты жіберу"
                    }

                }
            Text(
                text = text,
                color = Color.White
            )
        }


    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    HIVmanagerTheme {
        SignInScreenUi()
    }
}