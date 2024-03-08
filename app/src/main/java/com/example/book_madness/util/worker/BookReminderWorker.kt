package com.example.book_madness.util.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.book_madness.R

class BookReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        bookReminderNotification(
            applicationContext.resources.getString(R.string.reminder_description),
            applicationContext
        )
        return Result.success()
    }
}
