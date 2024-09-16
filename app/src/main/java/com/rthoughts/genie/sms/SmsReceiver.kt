package com.rthoughts.genie.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.rthoughts.genie.SharedData


class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val bundle = intent.extras
        Log.println(Log.INFO, "RRR", "Inside Receiver")

        if (bundle != null) {
            if (SharedData.smsAIFlag) {
                val sms = bundle.get("pdus") as Array<*>
                for (i in sms.indices) {
                    val format = bundle.getString("format")
                    val smsMessage = SmsMessage.createFromPdu(sms[i] as ByteArray, format)
                    val phoneNumber = smsMessage.displayOriginatingAddress
                    val messageText = smsMessage.messageBody.toString()
                    val date = smsMessage.timestampMillis
                    val smsData =
                        if (SharedData.receiver.isNotEmpty()) SmsData(
                            phoneNumber,
                            messageText,
                            date,
                            SharedData.receiver
                        ) else SmsData(phoneNumber, messageText, date)
                    context?.contentResolver?.let {
                        SmsAI(it, smsData)
                    }
                    Toast.makeText(
                        context,
                        "phoneNumber: $phoneNumber\nmessageText: $messageText",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

}