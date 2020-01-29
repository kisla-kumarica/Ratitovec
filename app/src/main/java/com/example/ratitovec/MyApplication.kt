package com.example.ratitovec

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class MyApplication : Application() {
    var isLight: Boolean = false

    companion object {
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        if (PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(
                getString(R.string.darkmode_key),
                false
            )
        )
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}