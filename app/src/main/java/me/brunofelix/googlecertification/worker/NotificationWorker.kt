package me.brunofelix.googlecertification.worker

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.brunofelix.googlecertification.R
import me.brunofelix.googlecertification.extension.sendNotification
import timber.log.Timber

class NotificationWorker (
    private val context: Context,
    private val parameters: WorkerParameters
) : Worker(context, parameters) {

    override fun doWork(): Result {
        return try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val workerTag = inputData.getLong(context.getString(R.string.key_worker_tag), 0)
            val taskName = inputData.getString(context.getString(R.string.key_task_name)) ?: "{TaskName}"

            applicationContext.sendNotification(taskId = workerTag, taskName = taskName)

            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }
}
