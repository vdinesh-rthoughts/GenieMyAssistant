package com.rthoughts.readallsms

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.rthoughts.readallsms.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val requestReadSms: Int = 2

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                requestReadSms
            )
        } else {
            setSmsMessages("", null)
        }

        mainBinding.allSms.setOnClickListener { setSmsMessages("", null) }
        mainBinding.outboxSms.setOnClickListener {
            setSmsMessages(
                "inbox",
                "body like '%OTP%' and read=1"
            )
        }
        mainBinding.inboxSms.setOnClickListener { setSmsMessages("sent", null) }
        mainBinding.draftSms.setOnClickListener {
            setSmsMessages(
                "inbox",
                "body LIKE '%OTP%' and read=0"
            )
        }
        mainBinding.sentSms.setOnClickListener { setSmsMessages("sent", "body LIKE '%time%'") }
        mainBinding.oneNumberSms.setOnClickListener {
            setSmsMessages("sent", "address LIKE ${getString((R.string.phone_number))}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestReadSms) {
            setSmsMessages("", null)
        }
    }

    private fun setSmsMessages(uriString: String, selection: String?) {
        val smsList = ArrayList<SmsData>()
        val cursor = contentResolver.query(
            Uri.parse("content://sms/$uriString"),
            null,
            selection,
            null,
            null
        )
        if (cursor != null) {
            println("SIZE: " + cursor.count)
            if (cursor.moveToFirst()) {
                /*println("CALC: " + Arrays.toString(cursor.columnNames))
                for (element in cursor.columnNames) {
                    println("$element : " + cursor.getString(cursor.getColumnIndex(element)))
                }*/
                val nameID = cursor.getColumnIndex("address")
                val messageID = cursor.getColumnIndex("body")
                val dateID = cursor.getColumnIndex("date")
                do {
                    val dateString = cursor.getString(dateID)
                    smsList.add(
                        SmsData(
                            cursor.getString(nameID),
                            Date(dateString.toLong()).toString(),
                            cursor.getString(messageID)
                        )
                    )
                } while (cursor.moveToNext())
            }
        } else {
            smsList.add(SmsData("NoUser", Date().toString(), "NoMessage"))
        }
        cursor?.close()
        val adapter = SmsListAdapter(this, smsList)
        mainBinding.smsListView.adapter = adapter
    }
}
