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

    lateinit var forService: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forService = Intent(this, MyService::class.java)

        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, "com.example.notification")
                .setMethodCallHandler { call, result ->
                    if (call.method == "startMusic") {
                        startMusicService()
                        result.success("Music Service Started")
                    } else if (call.method == "stopMusic") {
                        if (isServiceRunning()) {
                            print(isServiceRunning())
                            stopService(forService)
                        }
                        result.success("Music Service Stopped")
                    }
                }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
    }

    fun startMusicService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(forService)
        } else {
            startService(forService)
        }
    }

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