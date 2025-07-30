package io.github.caduviegas.carslist.infrastructure.background

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object SyncLeadScheduler {
    fun schedule(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<SyncLeadWorker>(
            1, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SyncLeadWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}