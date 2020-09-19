package com.rthoughts.genie.sms

import android.util.Log
import com.rthoughts.genie.BaseActivity
import java.util.*

class SmsAI(smsData: SmsData) : BaseActivity() {

    private val number = smsData.senderName
    private var message: String = smsData.message.trim()
    private var fn = ""

    //Genie send time
    //val date = smsData.date
    private val suffix = "Genie"

    init {
        if (message.startsWith(suffix)) {
            Log.println(Log.INFO, "AAA", "Genie Command")
            fn = message.replace(suffix, "", true).trim()
            Log.println(Log.INFO, "AAA", message)
            Log.println(Log.INFO, "AAAfnm", fn)


            if (fn.contains("send time", ignoreCase = true)) {
                Log.println(Log.INFO, "AAAs", "Genie sending sms")
                val date = Date().toString()
                Log.println(Log.INFO, "AAAd", date)
                SmsUtils().sendSms(number, date)

                Log.println(Log.INFO, "AAA", "Genie Command Processed")

            } else {
                Log.println(Log.INFO, "AAA", "Genie Command not proper")

            }
        } else {
            Log.println(Log.INFO, "AAA", "Genie Not Command")
        }
        Log.println(Log.INFO, "AAA", "OUT OF AI")

    }

}