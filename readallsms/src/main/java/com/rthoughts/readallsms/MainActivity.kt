package com.rthoughts.readallsms

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val requestReadSms: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS),
                requestReadSms)
        } else {
            setSmsMessages("", null)
        }
        all_sms.setOnClickListener { setSmsMessages("", null) }
        outbox_sms.setOnClickListener { setSmsMessages("inbox", "body like '%OTP%' and read=1") }
        inbox_sms.setOnClickListener { setSmsMessages("sent", null) }
        draft_sms.setOnClickListener { setSmsMessages("inbox", "body LIKE '%OTP%' and read=0") }
        sent_sms.setOnClickListener { setSmsMessages("sent", "body LIKE '%time%'") }
        one_number_sms.setOnClickListener {
            setSmsMessages("sent", "address LIKE ${getString((R.string.phone_number))}")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestReadSms) {
            setSmsMessages("", null)
        }
    }

    private fun setSmsMessages(uriString: String, selection: String?) {
        val smsList = ArrayList<SmsData>()
        val cursor = contentResolver.query(Uri.parse("content://sms/$uriString"),
            null,
            selection,
            null,
            null)
        if (cursor != null) {
            println("SIZA: " + cursor.count)
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
                    smsList.add(SmsData(cursor.getString(nameID),
                        Date(dateString.toLong()).toString(),
                        cursor.getString(messageID)))
                } while (cursor.moveToNext())
            }
        } else {
            smsList.add(SmsData("NoUser", Date().toString(), "NoMessage"))
        }
        cursor?.close()
        val adapter = ListAdapter(this, smsList)
        sms_list_view.adapter = adapter
    }
}
