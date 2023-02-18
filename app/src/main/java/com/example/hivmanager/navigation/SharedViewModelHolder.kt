package com.example.hivmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference
/** вспомогательный класс для хранения ссылки на viewModel,
 * при этом viewModel следует жизненному циклу hiltViewModel
 * */
class SharedViewModelHolder<T: ViewModel> {
    var sharedViewModelReference = WeakReference<T>(null)
}

@Composable
inline fun <reified T: ViewModel> SharedViewModelHolder<T>.createNewSharedViewModel(): T {
    val viewModel: T = hiltViewModel()
    LaunchedEffect(Unit) {
        if (sharedViewModelReference.get() !== viewModel) {
            sharedViewModelReference = WeakReference(viewModel)
        }
    }
    return viewModel
}

@Composable
inline fun <reified T : ViewModel> SharedViewModelHolder<T>.getSharedViewModel(): T {
    return sharedViewModelReference.get() ?: hiltViewModel()
}