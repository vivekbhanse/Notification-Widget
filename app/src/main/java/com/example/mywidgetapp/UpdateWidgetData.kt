package com.example.mywidgetapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class UpdateWidgetData(val context: Context) {
    var dataProcessor: DataProcessor? = null

    init {
        dataProcessor = DataProcessor(context)
    }

    fun updateWidget(updatedPrice: String? = null, isTimeUpdate: Boolean = false) {
        val intent = Intent(context, NewAppWidget::class.java).apply {
            action =  Constants.UPDATE_WIDGET
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (updatedPrice != null) {
            dataProcessor?.setStr(Constants.widget_text, updatedPrice)
        }
        if (isTimeUpdate) {
            saveCurrentTime()
        }
        pendingIntent.send()
    }

    private fun saveCurrentTime() {
        val currentTime = System.currentTimeMillis()
        dataProcessor?.setStr(Constants.last_activity_opened, currentTime.toString())
    }
}