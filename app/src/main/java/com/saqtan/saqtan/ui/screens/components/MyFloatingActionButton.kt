package com.saqtan.saqtan.ui.screens.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyFloatingActionButton(
    imageVector:ImageVector = Icons.Filled.Add,
    onClick:()->Unit = {}
){
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
        ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Add pill",
            tint = MaterialTheme.colors.background
        )
    }
}