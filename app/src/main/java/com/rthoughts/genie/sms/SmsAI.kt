package com.rthoughts.genie.sms

import android.content.ContentResolver
import android.util.Log
import com.rthoughts.genie.BaseActivity
import java.util.*

class SmsAI(mContentResolver: ContentResolver, smsData: SmsData) : BaseActivity() {

    private val number = smsData.senderName
    private var message: String = smsData.message.trim()
    private var fn = ""

    private val suffix = "Genie"

    init {
        if (message.startsWith(suffix)) {
            Log.println(Log.INFO, "AAA", "Genie Command")
            fn = message.replace(suffix, "", true).trim()
            when {
                fn.contains("send time", ignoreCase = true) -> {
                    Log.println(Log.INFO, "AAAs", "Genie sending sms")
                    val date = Date().toString()
                    Log.println(Log.INFO, "AAAd", date)
                    SmsUtils().sendSms(number, date)
                    Log.println(Log.INFO, "AAA", "Genie Send Time Command Processed")
                }
                fn.contains("send my name", ignoreCase = true) -> {
                    var contactDisplayName = getContactDisplayNameByNumber(mContentResolver, number)
                    contactDisplayName =
                        if (contactDisplayName.contains("NOT_FOUND")) "Hi, Sorry! I didn't save your number, I might missed your number!"
                        else "Dinesh Kumar Saved your number as \"$contactDisplayName\""
                    SmsUtils().sendSms(number, contactDisplayName)
                    Log.println(Log.INFO, "AAA", "Genie Contact Name Command Processed")
                }
                fn.contains("send code", ignoreCase = true) -> {
                    val smsDataRequired =
                        SmsUtils().getSmsFromDevice(mContentResolver, "body like '% code %'")
                    SmsUtils().sendSms(number, smsDataRequired.message)
                    Log.println(Log.INFO, "AAA", "Genie send code Command Processed")
                }
                fn.contains("send otp", ignoreCase = true) -> {
                    val smsDataRequired =
                        SmsUtils().getSmsFromDevice(mContentResolver, "body like '% otp %'")
                    SmsUtils().sendSms(number, smsDataRequired.message)
                    Log.println(Log.INFO, "AAA", "Genie send opt Command Processed")
                }
                else -> {
                    Log.println(Log.INFO, "AAA", "Genie Command not proper")
                }
            }
        } else {
            Log.println(Log.INFO, "AAA", "Genie Not Command")
        }
        Log.println(Log.INFO, "AAA", "OUT OF AI")
    }
}