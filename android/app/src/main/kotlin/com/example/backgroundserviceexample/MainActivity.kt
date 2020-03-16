package com.example.backgroundserviceexample

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {

    // Creating a service intent
    private lateinit var forService: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initalizing the service intent
        forService = Intent(this, MyService::class.java)

        // Method Channel implementation
        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, "music")
                .setMethodCallHandler { call, result ->
                    when (call.method) {
                        "startMusic" -> {
                            startMusicService()
                            result.success(isServiceRunning())
                        }
                        "stopMusic" -> {
                            if (isServiceRunning()) {
                                stopService(forService)
                            }
                            result.success(isServiceRunning())
                        }
                    }
                }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
    }

    // Starts music service
    fun startMusicService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(forService)
        } else {
            startService(forService)
        }
    }

    // Checks whether the music service is running
    fun isServiceRunning(): Boolean {
        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            print(service.service.className)
            if ("com.example.backgroundserviceexample.MyService" == service.service.className) {
                return true
            }
        }
        return false
    }
}
