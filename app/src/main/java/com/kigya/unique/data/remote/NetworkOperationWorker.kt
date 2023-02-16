package com.kigya.unique.data.remote

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.di.IoDispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class NetworkOperationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val lessonRepository: LessonRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(context, params) {

    override suspend fun doWork() = withContext(ioDispatcher) {
        return@withContext try {
            lessonRepository.getLessons()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
