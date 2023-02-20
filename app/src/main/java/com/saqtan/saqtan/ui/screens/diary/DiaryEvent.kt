package com.saqtan.saqtan.ui.screens.diary

import com.saqtan.saqtan.data.model.DiaryEntry

sealed class DiaryEvent {
    data class OnAddDiaryEntryClick(val diaryEntry: DiaryEntry):DiaryEvent()
    data class OnDeleteDiaryEntryClick(val diaryEntry: DiaryEntry):DiaryEvent()
}