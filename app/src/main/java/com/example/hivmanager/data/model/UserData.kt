package com.example.hivmanager.data.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserData(
    val pillInfoList: List<PillInfo> = listOf(),
    val height: Double = 0.0,
    val allergies:List<String> = listOf(),
    val diaryEntries: List<DiaryEntry> = listOf()
)

@Serializable
data class DiaryEntry(
    val upperTension: Int = 0,
    val lowerTension:Int = 0,
    val weight: Double = 0.0,
    val temperature: Double = 0.0,
    val comment:String = "",
    val time: String = ""
)