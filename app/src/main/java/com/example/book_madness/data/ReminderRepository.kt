package com.example.book_madness.data

import java.util.concurrent.TimeUnit

interface ReminderRepository {
    fun scheduleReminder(duration: Long, unit: TimeUnit)
}
