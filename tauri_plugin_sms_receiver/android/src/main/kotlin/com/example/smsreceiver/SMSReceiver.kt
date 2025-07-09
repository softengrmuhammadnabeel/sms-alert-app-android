package com.example.smsreceiver

import android.util.Log
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import java.io.IOException

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType

class SMSReceiver : BroadcastReceiver() {

    private val allowedNumbers = listOf("+923123372360") // Add allowed numbers here

    override fun onReceive(context: Context, intent: Intent) {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val pdus = bundle["pdus"] as Array<*>
            for (pdu in pdus) {

                val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                val sender = sms.displayOriginatingAddress
                val message = sms.messageBody
                Log.d("SMSReceiver", "Received SMS from: $sender\nMessage: $message")


                if (allowedNumbers.contains(sender)) {
                    sendToWebhook(sender, message)
                }
            }
        }
    }

    private fun sendToWebhook(sender: String, message: String) {
        val client = OkHttpClient()
        val json = """{"sender":"$sender","message":"$message"}"""

        // val mediaType = "application/json".toMediaType()
        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://192.168.0.105:3000/messageReceiver")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("SMSReceiver", "Failed to send webhook: ${e.message}", e)
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("SMSReceiver", "Webhook sent. Response code: ${response.code}")
        }
    })
    }
}
