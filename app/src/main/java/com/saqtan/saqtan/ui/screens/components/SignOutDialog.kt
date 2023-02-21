package com.saqtan.saqtan.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun SignOutDialog(
    dialogState: MaterialDialogState,
    onConfirmClick: ()->Unit
){
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties (
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        buttons = {
            positiveButton(text = "Cақтау", onClick = onConfirmClick)
            negativeButton(text = "Болдырмау")
        }
    ) {
        Text(
            text= "Сіз шыққыңыз келетініне сенімдісіз бе?",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}
