package com.saqtan.saqtan.data.model.notification

/** интерфейс для класса, который умеет назначать и отменять уведомления*/
interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}