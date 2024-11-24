package com.example.movieismylife

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.movieismylife.model.Movies

const val CHANNEL_ID = "movie_recommendation_channel"

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d("Notification", "Notification channel created")
    }
}

// 영화 추천 알림
fun showMovieRecommendationNotification(context: Context, movie: Movies) {
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_movie)
        .setContentTitle("영화 추천")
        .setContentText("오늘의 추천 영화: ${movie.title}")
        .setStyle(
            NotificationCompat.BigTextStyle()
            .bigText("${movie.title}\n\n${movie.overview}"))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    builder.setContentIntent(pendingIntent)

    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    )

    notificationManager?.notify(1, builder.build())
    Log.d("Notification", "알림에 나온 영화 제목: ${movie.title}")
}