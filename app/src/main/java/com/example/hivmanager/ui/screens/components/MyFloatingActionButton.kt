package com.example.hivmanager.ui.screens.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable

@Composable
fun MyFloatingActionButton(
    onClick:()->Unit = {}
){
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
        ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add pill",
            tint = MaterialTheme.colors.background
        )
    }
}