package com.example.hivmanager.data.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val pillInfoList: List<PillInfo> = listOf()
)