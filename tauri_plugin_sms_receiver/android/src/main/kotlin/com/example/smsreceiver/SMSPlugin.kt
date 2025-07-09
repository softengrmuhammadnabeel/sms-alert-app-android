package com.example.smsreceiver

import android.app.Activity
import android.content.IntentFilter
import android.Manifest
import android.provider.Telephony
import android.webkit.WebView
import app.tauri.annotation.TauriPlugin
import app.tauri.annotation.Permission
import app.tauri.plugin.Plugin

@TauriPlugin(
    permissions = [
        Permission(strings = [Manifest.permission.RECEIVE_SMS], alias = "receiveSms")
    ]
)
class SMSPlugin(private val activity: Activity): Plugin(activity) {
    override fun load(webView: WebView) {
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        activity.registerReceiver(SMSReceiver(), filter)
    }
}
