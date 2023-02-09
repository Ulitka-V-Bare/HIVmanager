package com.example.hivmanager.ui.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route

@Preview
@Composable
fun BottomNavBar(
    navigationEventSender: (NavigationEvent)->Unit = {},
    selected:Int = 0
){
    var selectedItem by remember { mutableStateOf(selected) }
    val items = listOf("Info", "Home", "Chat")
    val routes = listOf(Route.info,Route.home,Route.chat)
    BottomNavigation (
        backgroundColor = MaterialTheme.colors.background,
        content = {
            Row (
                modifier = Modifier.fillMaxSize(),
            ) {

                items.forEachIndexed { index, screen ->

                    // My Events
                    BottomNavigationItem (
                        selected = selectedItem == index,
                        onClick = {
                            if(selectedItem!=index) {
                                navigationEventSender(NavigationEvent.Navigate(routes[index]))
                            }
                            selectedItem = index

                                  },
                        selectedContentColor = MaterialTheme.colors.primaryVariant,
                        unselectedContentColor = Color(0xFF949494),
                        icon = {
                            when (index) {
                                0 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Info",
                                        tint = if (selectedItem == index) MaterialTheme.colors.primaryVariant
                                        else Color(0xFF949494)
                                    )
                                }
                                1 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Home",
                                        tint = if (selectedItem == index) MaterialTheme.colors.primaryVariant
                                        else Color(0xFF949494)
                                    )
                                }
                                2 -> {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Filled.Chat,
                                        contentDescription = "Chat",
                                        tint = if (selectedItem == index) MaterialTheme.colors.primaryVariant
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
