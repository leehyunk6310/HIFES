package com.ssafy.hifes.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.hifes.R
import com.ssafy.hifes.data.local.AppPreferences
import com.ssafy.hifes.ui.main.MainActivity


private const val TAG = "FirebaseMessageService_싸피"

class FirebaseMessageService : FirebaseMessagingService() {
    val CHANNEL_ID = "1"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        AppPreferences.initFcmToken(token)
    }

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            AppPreferences.initFcmToken(it)
            Log.d(TAG, "getFirebaseToken: ${it}")
        }
    }

    // Foreground, Background 모두 처리하기 위해서는 data에 값을 담아서 넘긴다.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var messageTitle = ""
        var messageContent = ""
        var lat = ""
        var lng = ""
        var festivalId = ""

        if (remoteMessage.notification != null) { // notification이 있는 경우 foreground처리
            //foreground
            messageTitle = remoteMessage.notification!!.title.toString()
            messageContent = remoteMessage.notification!!.body.toString()
            lat = remoteMessage.data.get("latitude").toString()
            lng = remoteMessage.data.get("longitude").toString()
            festivalId = remoteMessage.data.get("festivalId").toString()
            Log.d(TAG, "onMessageReceived: ${lat} ${lng} ${festivalId}")
            if(festivalId != "" && lat != "" && lng != ""){
                AppPreferences.saveCallLocation(lat, lng, festivalId)
            }

        } else {  // background 에 있을경우 혹은 foreground에 있을경우 두 경우 모두
            var data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data.get("title")}")
            Log.d(TAG, "data.message: ${data.get("body")}")

            messageTitle = data.get("title").toString()
            messageContent = data.get("body").toString()
            lat = data.get("latitude").toString()
            lng = data.get("longitude").toString()
            festivalId = data.get("festivalId").toString()

            Log.d(TAG, "onMessageReceived: ${lat} ${lng} ${festivalId}")
            if(festivalId != "" && lat != "" && lng != ""){
                AppPreferences.saveCallLocation(lat, lng, festivalId)
            }
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val mainPendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder
        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "hifes notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
            builder = NotificationCompat.Builder(this@FirebaseMessageService, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(this@FirebaseMessageService)
        }

        builder.setSmallIcon(R.drawable.logo_background_hifes)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        NotificationManagerCompat.from(this).apply {
            if (ActivityCompat.checkSelfPermission(
                    this@FirebaseMessageService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(101, builder.build())
        }

    }

}
