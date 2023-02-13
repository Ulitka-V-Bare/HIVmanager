package com.example.hivmanager.data.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val pillInfoList: List<PillInfo> = listOf(),
    val height: Double = 0.0,
    val weight: List<Double> = listOf(),
    val upperTension: List<Int> = listOf(),
    val lowerTension:List<Int> = listOf(),
    val allergies:List<String> = listOf()
)