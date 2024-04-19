package com.example.book_madness.model.util

import java.util.concurrent.TimeUnit

fun Long.toFormatTime(): String = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)
