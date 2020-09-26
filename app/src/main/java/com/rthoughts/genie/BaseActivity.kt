package com.rthoughts.genie

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import java.lang.Exception


open class BaseActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 102
    private val PERMISSIONS_REQUEST_SMS = 101

    fun smsPermissions(context: Context) {
        val send_sms = Manifest.permission.SEND_SMS
        val read_sms = Manifest.permission.READ_SMS
        val receive_sms = Manifest.permission.RECEIVE_SMS
        val permissionsNeeded = arrayOf(send_sms, receive_sms, read_sms)

        val permissionSendCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
        val permissionReceiveCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
        val permissionReadCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
        Log.println(Log.INFO,
            "PPP",
            "RECEIVE_SMS: $permissionReceiveCheck; SEND_SMS: $permissionSendCheck; READ_SMS: $permissionReadCheck")

        if (permissionSendCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "PPP", "Permission Granted for SEND_SMS")
        } else {
            Log.println(Log.INFO, "PPP", "Permission requesting for ALL_SMS")
            requestPermissions(context as Activity, permissionsNeeded, PERMISSIONS_REQUEST_SMS)
        }
    }

    fun requestContactPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(context as Activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            Toast.makeText(this, "Contact Permission already granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.println(Log.INFO,
            "PPP",
            "ON_Requesting_Permissions : $requestCode : ${permissions.size} : ${grantResults.size}")
        if (requestCode == PERMISSIONS_REQUEST_SMS) {
            for (i in 0..grantResults.size) {
                if (grantResults.size > i) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.println(Log.INFO,
                            "PPP",
                            "User given Permission Granded for ${permissions[i]}")
                    } else {
                        Log.println(Log.INFO,
                            "PPP",
                            "User reject Permission Granded for ${permissions[i]}")
                    }
                    val permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i])
                    Log.println(Log.INFO,
                        "PPP",
                        "Permission sms send after: $permissions[i] $permissionCheck")
                }

            }
        } else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contact Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                Log.println(Log.INFO, "PPP", "You have disabled a contacts permission")
            }
        } else {
            Log.println(Log.INFO, "PPP", "Permission code not match: $requestCode")
        }
    }


    fun getContactDisplayNameByNumber(mobileNumber: String?): String {
        var displayName = "NOT_FOUND"
        val dataContentUri: Uri = ContactsContract.Data.CONTENT_URI
        mobileNumber?.replace("+91", "")
        val whereClause =
            "mimetype='vnd.android.cursor.item/phone_v2' and replace(data1,' ','') like '%$mobileNumber%'"
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(dataContentUri, null, whereClause, null, null)
        } catch (e: Exception) {
            e.message?.let { Log.e("CCC", it) }
            e.printStackTrace()
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex("display_name"))
            }
        }
        cursor?.close()
        return displayName
    }
}