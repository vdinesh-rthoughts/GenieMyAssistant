package com.rthoughts.genie.sms

import android.content.ContentValues.TAG
import android.util.Log
import com.rthoughts.genie.SharedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


/*
class TeamsMessenger {
    val client: OkHttpClient = OkHttpClient()
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    fun sendMessageToTeams(webhookUrl: String?, message: String) {
        val jsonPayload = "{ \"text\": \"$message\" }"
        val body: RequestBody = RequestBody.create(jsonPayload, JSON)

        val request: Request =
            Request.Builder()
            .url(webhookUrl)
            .post(body)
            .build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        System.err.println("Unexpected code $response")
                    } else {
                        println("Message sent to Teams!")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
*/

fun sendMessageToTeams(webhookUrl: String, message: String, onResult: (Boolean) -> Unit) {
    Thread {
        try {
            val client = OkHttpClient()
            val json = """{ "text": "$message" }"""
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            Log.i(TAG, "sendMessageToTeams: ${response.isSuccessful}")
            onResult(response.isSuccessful)
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false)
        }
    }.start()
}