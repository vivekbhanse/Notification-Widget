package com.example.mywidgetapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    var priceText = "Initial Price"


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val edtPrice = findViewById<EditText>(R.id.edt_amount)
        val btnSubmit = findViewById<Button>(R.id.btn_priceSubmit)
        requestNotificationPermission()
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<WidgetUpdateWorker>(15, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WidgetUpdateWork", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest
        )

        aceessFcmTocken()
        btnSubmit.setOnClickListener {
            if (edtPrice.length() >= 1) {
                priceText = edtPrice.text.toString()
                UpdateWidgetData(this).updateWidget(priceText, true)
            }
        }

    }

    private fun aceessFcmTocken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get the FCM token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")

        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1
                )

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)
                println("check permission")
                println(areNotificationsEnabled())
                return@with
            }

        }

    }
    /** Here is request from postman can hit for get data notification
     * */

//    curl --location 'https://fcm.googleapis.com/fcm/send' \
//    --header 'Content-Type: application/json' \
//    --header 'Authorization: key=AAAALIE2vz0:APA91bHFjJT-SHfrd1KJOEbanS6Vh1rJ2oNrT85UGYPAm3TdEr674OJnOws8iKdYYJ6pFTYuV5qrPN9BgQFQ9jUXsUGM45rPTg3Z_OsajgRsHKVilvAhEJXRfg_vdYMi-xKfM_OkXyOK' \
//    --data '{
//    "to": "ehXtuZ62TYyeBcXlAYn4YB:APA91bGmSUyFPcctALFydSBEpdworsJQJJCWm4XBjEd5_vDoCEg6C5J4qcWUYNMv5F67poFmvjDej4iiDclIpJweWzQ4K-wKNwQLfKTjYZUTf4zJMfThBN4PDlkdTLqc3TUzgKEGGnEa",
//    "data": {
//        "priority": "high",
//        "sound": "app_sound.wav",
//        "content_available": true,
//        "bodyText": "1234",
//        "organization": "Elementary school"
//    }
//}'
}