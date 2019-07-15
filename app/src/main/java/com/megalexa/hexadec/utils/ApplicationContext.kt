package com.megalexa.hexadec.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("Registered")
class ApplicationContext: Application() {
    private var sContext: Context? = null

    override fun onCreate() {
        super.onCreate()
        sContext = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var sContext: Context? = null
            private set
    }
}