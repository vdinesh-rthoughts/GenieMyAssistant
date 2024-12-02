package com.rthoughts.genie.sms

import android.content.ContentResolver
import android.util.Log
import com.rthoughts.genie.BaseActivity
import com.rthoughts.genie.SharedData
import java.util.*

class SmsAILBG(mContentResolver: ContentResolver, smsData: SmsData) : BaseActivity() {

    private val number = smsData.senderNumber
    private var message: String = smsData.message.trim()
    private var receiver: String = smsData.receiverNumber.trim()
    private var fn = ""

    private val suffix = "Genie"
    private val lbgOtp = "Use passcode"
    private val lbgForwardOtp = "Forwarded_1: Use passcode"
    private val da1JourneyOTP = "To continue with your"
    private val da1JourneyForwardOTP = "Forwarded_1: To continue with your"
    private val o4bTempPwd = "Welcome to"
    private val o4bTempPwdFwd = "Forwarded_1: Welcome to"

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
        } else if ((message.startsWith(lbgOtp) || message.startsWith(o4bTempPwd) || message.startsWith(
                da1JourneyOTP
            )) && SharedData.forwardOriginalMessage
        ) {
            SmsUtils().sendSms(receiver, "Forwarded_1: $message")

            /*val whatsAppIntent = Intent(Intent.ACTION_SEND)
            whatsAppIntent.type = "text/plain"
            whatsAppIntent.putExtra(Intent.EXTRA_TEXT, message)
            whatsAppIntent.setPackage("com.whatsapp")
            try {
                this.startActivity(whatsAppIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }*/

        } else if ((message.startsWith(lbgForwardOtp) || message.startsWith(o4bTempPwdFwd) || message.startsWith(
                da1JourneyForwardOTP
            )) && SharedData.forwardForwardedMessage
        ) {
            SmsUtils().sendSms(
                receiver,
                "${message.replace("Forwarded_1", "Forwarded_2")}."
            )
        } else {
            Log.println(Log.INFO, "AAA", "Genie Not Command")
        }
        Log.println(Log.INFO, "AAA", "OUT OF AI")
    }
}