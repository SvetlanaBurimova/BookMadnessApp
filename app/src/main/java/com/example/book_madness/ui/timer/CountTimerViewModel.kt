package com.example.book_madness.ui.timer

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.book_madness.data.ReminderRepository
import com.example.book_madness.model.Reminder
import com.example.book_madness.model.util.toFormatTime
import com.example.book_madness.util.TIME_COUNTDOWN

class CountTimerViewModel(private val reminderRepository: ReminderRepository) : ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    private val _time = mutableStateOf(TIME_COUNTDOWN.toFormatTime())
    val time: MutableState<String> = _time

    private val _progress = mutableFloatStateOf(1.00F)
    val progress: MutableState<Float> = _progress

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: MutableState<Boolean> = _isPlaying

    private val _celebrate = mutableStateOf(false)
    val celebrate: MutableState<Boolean> = _celebrate

    fun handleCountDownTimer() {
        if (isPlaying.value) {
            pauseTimer()
            _celebrate.value = false
        } else {
            startTimer()
        }
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(
            false,
            TIME_COUNTDOWN.toFormatTime(),
            1.0F,
            false
        )
    }

    private fun startTimer() {
        _isPlaying.value = true
        countDownTimer =
            object : CountDownTimer(
                TIME_COUNTDOWN,
                1000
            ) {
                override fun onTick(millisRemaining: Long) {
                    val progressValue = (millisRemaining - 1000).toFloat() / TIME_COUNTDOWN
                    handleTimerValues(true, millisRemaining.toFormatTime(), progressValue, false)
                    _celebrate.value = false
                }

                override fun onFinish() {
                    pauseTimer()
                    _celebrate.value = true
                }
            }.start()
    }

    private fun handleTimerValues(
        isPlaying: Boolean,
        time: String,
        progress: Float,
        celebrate: Boolean,
    ) {
        _isPlaying.value = isPlaying
        _time.value = time
        _progress.floatValue = progress
        _celebrate.value = celebrate
    }

    fun scheduleReminder(reminder: Reminder) {
        reminderRepository.scheduleReminder(reminder.duration, reminder.unit)
    }
}
