package com.example.simplebanking

import android.app.Application
import com.example.simplebanking.di.initKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
