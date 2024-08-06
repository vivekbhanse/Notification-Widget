package com.example.mywidgetapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NewAppWidget : AppWidgetProvider() {
    var dataProcessor: DataProcessor? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent != null) {
            dataProcessor = context?.let { DataProcessor(it) }
            if (intent.action == Constants.UPDATE_WIDGET) {

                val appWidgetManager = AppWidgetManager.getInstance(context)
                val componentName = context?.let { ComponentName(it, NewAppWidget::class.java) }
                val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context?.packageName, R.layout.app_widget_amount)
                    val distime = displayPriceTime(context)
                    views.setTextViewText(R.id.appwidget_text, "$" + distime.first)
                    views.setTextViewText(
                        R.id.txt_lastUpdatbyValue, distime.second
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        dataProcessor = context?.let { DataProcessor(it) }
        val views = RemoteViews(context?.packageName, R.layout.app_widget_amount)
        val distime = displayPriceTime(context)
        views.setTextViewText(R.id.appwidget_text, "$" + distime.first)
        views.setTextViewText(R.id.txt_lastUpdatbyValue, distime.second)
        appWidgetManager?.updateAppWidget(appWidgetIds, views)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?,
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    private fun displayPriceTime(context: Context?): Pair<String?, String?> {
        val lastOpenedTimeMillis = dataProcessor?.getStr(Constants.last_activity_opened)
        var lastOpenedTime: String? = null
        val latestText = dataProcessor?.getStr(Constants.widget_text)
        if (lastOpenedTimeMillis != null) {
            lastOpenedTime = getTimeDifferenceInWords(lastOpenedTimeMillis.toLong())
        }
        return Pair(latestText, lastOpenedTime)
    }

    fun getTimeDifferenceInWords(timestampMillis: Long): String {
        val now = System.currentTimeMillis()
        val past = Date(timestampMillis)
        val diffInMillis = now - timestampMillis
        val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        return when {
            diffInSeconds < 60 -> "Just now"
            diffInSeconds < 120 -> "Minute Ago"
            diffInSeconds < 3600 -> "${TimeUnit.SECONDS.toMinutes(diffInSeconds)} minutes ago"
            diffInSeconds < 86400 -> "${TimeUnit.SECONDS.toHours(diffInSeconds)} hours ago"
            diffInSeconds < 604800 -> "${TimeUnit.SECONDS.toDays(diffInSeconds)} days ago"
            else -> {
                val sdf = SimpleDateFormat(Constants.widget_date, Locale.getDefault())
                "On ${sdf.format(past)}"
            }
        }
    }


}