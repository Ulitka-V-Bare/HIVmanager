package com.example.hivmanager.ui.screens.diary

import com.example.hivmanager.data.model.DiaryEntry

sealed class DiaryEvent {
    data class OnAddDiaryEntryClick(val diaryEntry: DiaryEntry):DiaryEvent()
    data class OnDeleteDiaryEntryClick(val diaryEntry: DiaryEntry):DiaryEvent()
}