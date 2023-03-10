package com.saqtan.saqtan.ui.screens.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SignInButton(
    onClick:()->Unit = {},
    modifier: Modifier = Modifier,
    enabled:Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier=modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(

        ),
        enabled=enabled,
        content = content,
        //border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)),
        shape = RoundedCornerShape(10.dp),
    )
}


