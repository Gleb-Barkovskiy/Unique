package com.kigya.unique.usecase

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kigya.unique.data.remote.NetworkOperationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleWorkUseCase @Inject constructor(
    private val workManager: WorkManager,
) {
    operator fun invoke() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val dataWorker =
            PeriodicWorkRequestBuilder<NetworkOperationWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(getDelayUntilNext8PM(), TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()
        workManager.enqueue(dataWorker)
    }

    private fun getDelayUntilNext8PM(): Long {
        val now = Calendar.getInstance()
        val next8PM = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return next8PM.timeInMillis - now.timeInMillis
    }
}
