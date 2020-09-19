package com.rthoughts.genie.sms

import android.content.Context
import android.net.Uri
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.rthoughts.genie.BaseActivity

class SmsUtils : BaseActivity() {


    fun sendSms(toNumber: String?, message: String) {
        if (toNumber == "" || message == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            Log.println(Log.INFO, "SSS", "planning to sent sms $message to $toNumber")
            val toNumber1 = toNumber?.replace("+91", "")
            Log.println(Log.INFO, "SSSDDD", "is number digital only: $toNumber1 : " + TextUtils.isDigitsOnly(toNumber1))

            if (TextUtils.isDigitsOnly(toNumber1)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(toNumber1, null, message, null, null)
                Log.println(Log.INFO, "SSS", "Message send successfully $message to $toNumber")
            } else {
                Log.println(Log.INFO, "SSSException", "Message NOT successfully $message to $toNumber")
            }
        }
    }

    private fun getSmsFromDevice(selection: String?): SmsData {
        val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, selection, null, null)
        var number = ""
        var message = ""
        var date: Long = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val numberID = cursor.getColumnIndex("address")
                val messageID = cursor.getColumnIndex("body")
                val dateID = cursor.getColumnIndex("date")

                number = cursor.getString(numberID)
                message = cursor.getString(messageID)
                date = cursor.getString(dateID).toLong()
            }
        } else {
            Log.i("SMS","No message in the device")
        }
        cursor?.close()
        return SmsData(number, message, date)
    }
}