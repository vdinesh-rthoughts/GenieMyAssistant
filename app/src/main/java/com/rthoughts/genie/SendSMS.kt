package com.rthoughts.genie

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.rthoughts.genie.sms.SmsUtils
import kotlinx.android.synthetic.main.activity_send_sms.*

class SendSMS : BaseActivity() {

    private val permissionRequest = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_sms)
        val send = findViewById<Button>(R.id.btnSend)
        send.setOnClickListener {
            myMessage()
        }
    }


    private fun myMessage() {
        val myNumber: String = editTextPhone.text.toString().trim()
        val myMsg: String = editTextMessage.text.toString().trim()
        if (!(myNumber != "" && myMsg != "")) {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                SmsUtils().sendSms(myNumber, myMsg)
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessage()
            } else {
                Toast.makeText(this,
                    "You don't have required permission to send a message",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}