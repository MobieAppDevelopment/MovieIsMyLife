package com.example.movieismylife

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

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

fun showMovieRecommendationNotification(context: Context) {
    //Todo: RandomMovie 선정하는 로직 생성 필요. (영화 10개 정도 List 만든 다음 Random())으로 하나 가져오면 될듯
    val randomMovie = "유령신부"
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_movie)
        .setContentTitle("영화 추천")
        .setContentText("오늘의 추천 영화: $randomMovie")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    )

    notificationManager?.notify(1, builder.build())
    Log.d("Notification", "Notification shown")
}
