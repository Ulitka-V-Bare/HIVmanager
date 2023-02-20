package com.saqtan.saqtan.ui.screens.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saqtan.saqtan.R
import com.saqtan.saqtan.data.model.Articles
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.ui.screens.components.BottomNavBar
import com.saqtan.saqtan.ui.screens.components.MyTopAppBar
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme

@Composable
fun InfoScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: InfoViewModel = hiltViewModel()
) {
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
    InfoScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
    )
}


@Composable
private fun InfoScreenUi(
    bottomNavBarNavigationEventSender: (NavigationEvent) -> Unit = {}
) {
    val articles = Articles
    Scaffold(
        topBar = { MyTopAppBar("Справка") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender, 0) }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painterResource(id = R.drawable.article_logo),
                contentDescription = "Big logo",
                contentScale = ContentScale.Crop
            )
            articles.forEach { article ->
                var isExpanded by remember{ mutableStateOf(false)}
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = article.header, modifier = Modifier.padding(start=8.dp))
                            IconButton(onClick = { isExpanded=!isExpanded }) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = "expend more/less article text",
                                    tint = if(isExpanded) MaterialTheme.colors.primaryVariant else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                                )
                            }
                        }
                        AnimatedVisibility(visible = isExpanded) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = article.text, modifier = Modifier.padding(start=8.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }


    }
}

@Preview
@Composable
private fun InfoScreenPreview(){
    HIVmanagerTheme {
        InfoScreenUi()
    }
}