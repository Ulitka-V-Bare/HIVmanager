package com.saqtan.saqtan.data.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

/** класс с всей информацией о пользователей(не включая информацию врача)
 * */
@Serializable
data class UserData(
    val pillInfoList: List<PillInfo> = listOf(),
    val height: Int = 0,
    val allergies:String = "",
    val diaryEntries: List<DiaryEntry> = listOf()
)
/** класс для хранения данных о записи в дневнике наблюдений
 * */
@Serializable
data class DiaryEntry(
    val upperTension: Int = 0,
    val lowerTension:Int = 0,
    val weight: Double = 0.0,
    val temperature: Double = 0.0,
    val pulse: Int = 0,
    val comment:String = "",
    val time: String = "default value"
)
/** создает экземпляр UserData по данным из firebase
 * */
fun constructUserDataFromFirestore(result: DocumentSnapshot):UserData{
    val map = result.get("data") as Map<*, *>
    Log.d("construct","enter")
    val allergies = map["allergies"].toString()
    Log.d("construct","$allergies")
    val height = map["height"].toString().toDouble().toInt()
    Log.d("construct","$height")
    val diaryEntries = map["diaryEntries"] as List<Map<*,*>>
    val myDiaryEntries = mutableListOf<DiaryEntry>()
    val myPillsList = mutableListOf<PillInfo>()
    for(entry in diaryEntries){
        val temperature = entry["temperature"].toString().toDouble()
        val pulse = entry["pulse"].toString().toDouble().toInt()
        val weight = entry["weight"].toString().toDouble()
        val comment = entry["comment"].toString()
        val time = entry["time"].toString()
        val lowerTension = entry["lowerTension"].toString().toDouble().toInt()
        val upperTension = entry["upperTension"].toString().toDouble().toInt()
        val diaryEntry = DiaryEntry(upperTension, lowerTension, weight, temperature, pulse, comment, time)
        myDiaryEntries.add(diaryEntry)
        Log.d("construct","$diaryEntry")
    }
    val pillInfoList = map["pillInfoList"] as List<Map<*,*>>
    for(pillInfo in pillInfoList) {
        val duration = pillInfo["duration"].toString().toDouble().toInt()
        val timeToTakePill = pillInfo["timeToTakePill"] as List<*>
        val timeList = mutableListOf<String>()
        for(time in timeToTakePill){
            timeList.add(time.toString())
        }
        val name = pillInfo["name"].toString()
        val finishDate = pillInfo["finishDate"].toString()
        val startDate = pillInfo["startDate"].toString()
        val myPillInfo = PillInfo(name, startDate, finishDate,timeList,duration)
        Log.d("construct","$myPillInfo")
        myPillsList.add(myPillInfo)
    }
    Log.d("construct","finish")
    return UserData(
        myPillsList,
        height,
        allergies,
        myDiaryEntries
    )
}