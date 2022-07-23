package me.brunofelix.googlecertification.extension

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import me.brunofelix.googlecertification.R
import me.brunofelix.googlecertification.ui.taskdetails.TaskDetailsActivity
import me.brunofelix.googlecertification.util.AppConstants

fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelName = getString(R.string.channel_name)
        val channelDescription = resources.getString(R.string.channel_description)

        val channel = NotificationChannel(
            getString(R.string.channel_id),
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(true)
            enableVibration(true)
            lightColor = Color.GREEN
            description = channelDescription
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun Context.sendNotification(taskId: Long, taskName: String) {
    val intent = Intent(this, TaskDetailsActivity::class.java).apply {
        putExtra(AppConstants.TASK_ID, taskId)
    }
    val pendingIntent = TaskStackBuilder.create(this).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
        .setSmallIcon(R.drawable.ic_android)
        .setContentTitle(taskName)
        .setColor(ContextCompat.getColor(this, R.color.purple_500))
        .setContentText(getString(R.string.notification_content))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(this)) {
        notify(taskId.toInt(), builder.build())
    }
}

fun Context.cancelNotification(notificationId: Int) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(notificationId)
}
