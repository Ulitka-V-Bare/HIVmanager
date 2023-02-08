package com.example.hivmanager.ui.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun BottomNavBar(){
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Info", "Home", "Chat")
    BottomNavigation (
        backgroundColor = Color.White,
        content = {
            Row (
                modifier = Modifier.fillMaxSize(),
            ) {

                items.forEachIndexed { index, screen ->

                    // My Events
                    BottomNavigationItem (
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        selectedContentColor = Color(0xFF136acb),
                        unselectedContentColor = Color(0xFF949494),
                        icon = {
                            when (index) {
                                0 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Info",
                                        tint = if (selectedItem == index) Color(0xFF136acb)
                                        else Color(0xFF949494)
                                    )
                                }
                                1 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Home",
                                        tint = if (selectedItem == index) Color(0xFF136acb)
                                        else Color(0xFF949494)
                                    )
                                }
                                2 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Chat,
                                        contentDescription = "Chat",
                                        tint = if (selectedItem == index) Color(0xFF136acb)
                                        else Color(0xFF949494)
                                    )
                                }
                            }

                        }
                    )


                }

            }

        }
    )
}
