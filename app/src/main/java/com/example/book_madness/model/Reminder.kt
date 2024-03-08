package com.example.book_madness.model

import androidx.annotation.StringRes
import java.util.concurrent.TimeUnit

data class Reminder(
    @StringRes val durationRes: Int,
    val duration: Long,
    val unit: TimeUnit
)
