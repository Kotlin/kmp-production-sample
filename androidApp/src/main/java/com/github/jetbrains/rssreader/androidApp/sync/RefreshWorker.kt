package com.github.jetbrains.rssreader.androidApp.sync

import android.content.Context
import androidx.work.*
import com.github.jetbrains.rssreader.core.RssReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class RefreshWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {
    private val rssReader: RssReader by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.Main) {
        rssReader.getAllFeeds(true)
        Result.success()
    }

    companion object {
        private const val WORK_NAME = "refresh_work_name"
        fun enqueue(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.HOURS).build()
            )
        }
    }
}