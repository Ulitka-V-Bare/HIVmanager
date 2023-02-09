package com.example.hivmanager.ui.screens.components

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle

@Composable
fun SignInTextField(
    value:String,
    onValueChange:(String)->Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        singleLine = true,
        isError = isError,
        label = label,
        keyboardActions = setDefaultKeyboardActionIfNull(keyboardActions = keyboardActions),
        modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            focusedBorderColor = MaterialTheme.colors.primaryVariant
        )

    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun setDefaultKeyboardActionIfNull(keyboardActions: KeyboardActions):KeyboardActions{
    val keyboardController = LocalSoftwareKeyboardController.current
    var onDone:(KeyboardActionScope.() -> Unit)? = keyboardActions.onDone
    if(onDone==null){
        onDone = {keyboardController?.hide()}
    }
    return KeyboardActions(
        onDone = onDone,
        onSend = keyboardActions.onSend,
        onNext = keyboardActions.onNext,
        onGo = keyboardActions.onGo,
        onPrevious = keyboardActions.onPrevious,
        onSearch = keyboardActions.onSearch
    )
}