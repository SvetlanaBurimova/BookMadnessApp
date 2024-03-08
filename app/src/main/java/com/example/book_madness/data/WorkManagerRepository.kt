package com.example.book_madness.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.book_madness.util.worker.BookReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerRepository(context: Context): ReminderRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleReminder(duration: Long, unit: TimeUnit) {
        val workRequestBuilder = OneTimeWorkRequestBuilder<BookReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(Data.Builder().build())
            .build()
        workManager.enqueueUniqueWork(
            "$duration",
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )
    }
}
