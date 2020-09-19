package com.rthoughts.genie

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GeniePermissions : AppCompatActivity() {


    fun smsSendPermission(content: Context) {
        val permissionCheck = ContextCompat.checkSelfPermission(content, Manifest.permission.SEND_SMS)
        Log.println(Log.INFO, "EEE", "Permission sms send before: $permissionCheck")
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "EEE", "Permission Granded for SEND_SMS")
        } else {
            Log.println(Log.INFO, "EEE", "Permission requesting for SEND_SMS")
            ActivityCompat.requestPermissions(content as Activity, arrayOf(Manifest.permission.SEND_SMS), 101)
        }
        val permissionCheck1 = ContextCompat.checkSelfPermission(content, Manifest.permission.SEND_SMS)
        Log.println(Log.INFO, "EEE", "Permission sms send after: $permissionCheck1")
    }


    fun smsReceivePermission(content: Context) {
        val permissionCheck = ContextCompat.checkSelfPermission(content, Manifest.permission.RECEIVE_SMS)
        Log.println(Log.INFO, "EEE", "Permission sms receive before: $permissionCheck")
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "EEE", "Permission Granded for RECEIVE_SMS")
        } else {
            Log.println(Log.INFO, "EEE", "Permission requesting for RECEIVE_SMS")
            ActivityCompat.requestPermissions(content as Activity, arrayOf(Manifest.permission.RECEIVE_SMS), 2)
        }
        val permissionCheck1 = ContextCompat.checkSelfPermission(content, Manifest.permission.RECEIVE_SMS)
        Log.println(Log.INFO, "EEE", "Permission sms receive after: $permissionCheck1")
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.println(Log.INFO, "EEE", "ON_Requesting_SMS : $requestCode : ${permissions.size} : ${grantResults.size}")

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.println(Log.INFO, "EEE", "User given Permission Granded for SEND_SMS")
            } else {
                Log.println(Log.INFO, "EEE", "User reject Permission Granded for SEND_SMS")
            }
        }

    }


}