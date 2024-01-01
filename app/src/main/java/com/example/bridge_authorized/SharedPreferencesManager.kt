package com.example.bridge_authorized

import android.content.Context

object SharedPreferencesManager {
    private const val PREFERENCES_FILE_KEY = "com.example.bridge_authorized.PREFERENCE_FILE_KEY"

    fun saveDonationData(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getDonationData(context: Context,img:Int ,key: String): String {
        val sharedPref = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString(key, "") ?: ""
    }
}
