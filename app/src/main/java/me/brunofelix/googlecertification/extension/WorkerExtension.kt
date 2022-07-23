package me.brunofelix.googlecertification.extension

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import me.brunofelix.googlecertification.R
import me.brunofelix.googlecertification.data.Task
import me.brunofelix.googlecertification.worker.NotificationWorker
import java.util.concurrent.TimeUnit

fun Context.scheduleWorker(task: Task, workerTag: Long) {
    val inputData = Data.Builder().apply {
        putLong(getString(R.string.key_worker_tag), workerTag)
        putString(getString(R.string.key_task_name), task.name)
    }

    val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
        .setInitialDelay(task.duration, TimeUnit.MINUTES)
        .setInputData(inputData.build())
        .addTag(workerTag.toString())
        .build()
    WorkManager.getInstance(this).enqueue(notificationWork)
}

fun Context.cancelWorker(workerTag: Long) {
    WorkManager.getInstance(this).cancelAllWorkByTag(workerTag.toString())
}
