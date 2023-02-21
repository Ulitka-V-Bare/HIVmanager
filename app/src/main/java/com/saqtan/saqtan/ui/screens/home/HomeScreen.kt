package com.saqtan.saqtan.ui.screens.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.saqtan.saqtan.R
import com.saqtan.saqtan.data.model.UserData
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.navigation.Route
import com.saqtan.saqtan.ui.screens.components.BottomNavBar
import com.saqtan.saqtan.ui.screens.components.MyTopAppBar
import com.saqtan.saqtan.ui.screens.components.SignOutDialog
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@Composable
fun HomeScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
    /***
     * перенапрявляет в настройки уведомлений этого приложения
     */
    fun openNotificationChannelSettings(context: Context) {
        val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
        //    .putExtra(Settings.EXTRA_CHANNEL_ID, Constants.CHANNEL_ID)
        context.startActivity(intent)
    }

    val state = viewModel.userRepository.userDataFlow.collectAsState(initial = UserData()).value

    HomeScreenUi(
        bottomNavBarNavigationEventSender = { viewModel.sendNavigationEvent(it) },
        onOpenPillListClick = { viewModel.sendNavigationEvent(NavigationEvent.Navigate(Route.pillReminder)) },
        onOpenNotificationChannelSettingsClick = { openNotificationChannelSettings(context) },
        onOpenDiaryClick = { viewModel.sendNavigationEvent(NavigationEvent.Navigate(Route.diary)) },
        onConfirmEditHeightClick = { viewModel.onEvent(HomeEvent.OnConfirmEditHeightClick(it)) },
        onConfirmEditAllergiesClick = { viewModel.onEvent(HomeEvent.OnConfirmEditAllergiesClick(it)) },
        userHeight = state.height,
        userAllergies = state.allergies,
        onSignOutClick = {viewModel.onEvent(HomeEvent.OnSignOutClick)}
    )
}


@Composable
private fun HomeScreenUi(
    bottomNavBarNavigationEventSender: (NavigationEvent) -> Unit = {},
    onOpenPillListClick: () -> Unit = {},
    onOpenNotificationChannelSettingsClick: () -> Unit = {},
    onOpenDiaryClick: () -> Unit = {},
    userHeight: Int = 0,
    userAllergies: String = "",
    onConfirmEditAllergiesClick: (String) -> Unit = {},
    onConfirmEditHeightClick: (String) -> Unit = {},
    onSignOutClick:()->Unit = {}
) {
    val dialogState = rememberMaterialDialogState()
    SignOutDialog(dialogState = dialogState, onConfirmClick = onSignOutClick)
    Scaffold(
        topBar = { MyTopAppBar("Басты бет") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender, 1) },
        floatingActionButton = { FloatingActionButton(
            onClick = {dialogState.show()},
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 15))
        ) {
            Text(text = "Шығу",modifier = Modifier.padding(horizontal = 24.dp), color = Color.White)
        }},
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HomeButton(
                onClick = onOpenDiaryClick,
                text = "Күнделік",
                icon = Icons.Filled.EditNote
            )
            HomeButton(
                onClick = onOpenPillListClick,
                text = "Менің ескертулерім",
                icon = painterResource(id = R.drawable.pill)
            )
            /***
             * lifecycleState и state необходимы для рекомпозиции при изменении состояния цикла,
             * в данном случае, чтобы перерисовывать экран после перехода из настроек оповещений обратно
             * на HomeScreen
             */
            val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
            val state = lifecycleState.value //нигде не используется, но не удалять!
            HomeButton(
                onClick = onOpenNotificationChannelSettingsClick,
                text = if(checkIfNotificationsEnabled())"Eскертулер баптаулары" else "Eскертулер өшіп тұр",
                icon = if(checkIfNotificationsEnabled())Icons.Filled.Settings else Icons.Filled.Warning,
                tint = if(checkIfNotificationsEnabled()) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.error
            )

            heightContainer(userHeight, { onConfirmEditHeightClick(it) })
            allergiesContainer(userAllergies, { onConfirmEditAllergiesClick(it) })
        }
    }
}
@Composable
private fun checkIfNotificationsEnabled():Boolean{
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
        return LocalContext.current.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    else
        return NotificationManagerCompat.from(LocalContext.current).areNotificationsEnabled()
}

/**
 * вспомогательная функция для наблюдения за жизненным циклом
 */
@Composable
fun Lifecycle.observeAsState(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsState.addObserver(observer)
        onDispose {
            this@observeAsState.removeObserver(observer)
        }
    }
    return state
}

@Composable
private fun HomeButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    icon: ImageVector = Icons.Filled.Close,
    tint: Color = MaterialTheme.colors.primaryVariant
) {


    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text)
        }
    }
}

@Composable
private fun HomeButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    icon: Painter
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = icon, contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colors.primaryVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text)
        }
    }
}


@Composable
private fun allergiesContainer(
    userAllergies: String = "",
    onConfirmEditAllergiesClick: (String) -> Unit = {}
) {

    var isEditingHeight by remember { mutableStateOf(false) }

    if (!isEditingHeight) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(5f)) {
                Text(
                    text = "Менің аллергияларым: $userAllergies,",
                    modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                IconButton(
                    onClick = { isEditingHeight = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit height button",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }

    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Менің аллергияларым: ",
            )
            var tempAllergies by remember { mutableStateOf(userAllergies) }
            TextField(
                value = tempAllergies,
                onValueChange = { value ->
                    tempAllergies = value
                },
                trailingIcon = {
                    Row() {
                        IconButton(onClick = { isEditingHeight = false }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close height button"
                            )
                        }
                        IconButton(onClick = {
                            onConfirmEditAllergiesClick(tempAllergies)
                            isEditingHeight = false
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "close height button"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                //  modifier = Modifier.padding(end=50.dp)
            )
        }

    }
}


@Composable
private fun heightContainer(
    userHeight: Int = 0,
    onConfirmEditHeightClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isEditingHeight by remember { mutableStateOf(false) }
        Text(
            text = "Менің бой өлшемім: ",
        )
        if (!isEditingHeight) {
            Text(
                text = "$userHeight"
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { isEditingHeight = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit height button"
                )
            }
        } else {
            var tempHeight by remember { mutableStateOf(if (userHeight == 0) "" else userHeight.toString()) }
            TextField(
                value = tempHeight,
                onValueChange = { value ->
                    if (value.length <= 3 && isAllDigits(value))
                        tempHeight = value
                },
                trailingIcon = {
                    Row() {
                        IconButton(onClick = { isEditingHeight = false }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close height button"
                            )
                        }
                        IconButton(onClick = {
                            onConfirmEditHeightClick(tempHeight)
                            isEditingHeight = false
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "close height button"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
            )
        }

    }
}

private fun isAllDigits(s: String): Boolean {
    for (char in s) {
        if (!char.isDigit()) return false
    }
    return true
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HIVmanagerTheme {
        HomeScreenUi()
    }
}