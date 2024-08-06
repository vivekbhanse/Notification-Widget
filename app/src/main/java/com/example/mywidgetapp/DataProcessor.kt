package com.example.mywidgetapp

import android.content.Context

class DataProcessor(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "appname_prefs"
    }

    // Check if a key exists in SharedPreferences
    fun sharedPreferenceExists(key: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(key)
    }

    // Set an integer value in SharedPreferences
    fun setInt(key: String, value: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(key, value)
            apply()
        }
    }

    // Get an integer value from SharedPreferences
    fun getInt(key: String): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    // Set a string value in SharedPreferences
    fun setStr(key: String, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    // Get a string value from SharedPreferences
    fun getStr(key: String): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, "DNF") ?: "DNF"
    }

    // Set a boolean value in SharedPreferences
    fun setBool(key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    // Get a boolean value from SharedPreferences
    fun getBool(key: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }

}