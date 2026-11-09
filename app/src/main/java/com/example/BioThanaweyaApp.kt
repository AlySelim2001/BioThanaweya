package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp(Application::class)
class BioThanaweyaApp : Hilt_BioThanaweyaApp() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
        } catch (e: Exception) {
            Log.d("BioThanaweyaApp", "Optional FirebaseApp init notice: ${e.message}")
        }
    }
}

