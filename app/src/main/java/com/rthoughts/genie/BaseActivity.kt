package com.rthoughts.genie

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat


open class BaseActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 102
    private val PERMISSIONS_REQUEST_SMS = 101

    fun smsSendPermission(content: Context) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(content, Manifest.permission.SEND_SMS)
        Log.println(Log.INFO, "PPP", "Permission sms send before: $permissionCheck")
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "PPP", "Permission Granded for SEND_SMS")
        } else {
            Log.println(Log.INFO, "PPP", "Permission requesting for SEND_SMS")
            requestPermissions(
                content as Activity,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSIONS_REQUEST_SMS
            )
        }
    }

    fun smsReceivePermission(content: Context) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(content, Manifest.permission.RECEIVE_SMS)
        Log.println(Log.INFO, "PPP", "Permission sms receive before: $permissionCheck")
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "PPP", "Permission Granded for RECEIVE_SMS")
        } else {
            Log.println(Log.INFO, "PPP", "Permission requesting for RECEIVE_SMS")
            requestPermissions(
                content as Activity,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                PERMISSIONS_REQUEST_SMS
            )
        }
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.println(Log.INFO, "PPP", "ON_Requesting_SMS : $requestCode : ${permissions.size} : ${grantResults.size}")

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.println(Log.INFO, "PPP", "User given Permission Granded for SEND_SMS")
            } else {
                Log.println(Log.INFO, "PPP", "User reject Permission Granded for SEND_SMS")
            }
            val permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            Log.println(Log.INFO, "PPP", "Permission sms send after: $permissionCheck1")
        }

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.println(Log.INFO, "PPP", "User given Permission Granded for RECEIVE_SMS")
            } else {
                Log.println(Log.INFO, "PPP", "User reject Permission Granded for RECEIVE_SMS")
            }
            val permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            Log.println(Log.INFO, "PPP", "Permission sms receive after: $permissionCheck1")
        }


    }*/

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
        Log.println(
            Log.INFO,
            "PPP",
            "RECEIVE_SMS: $permissionReceiveCheck; SEND_SMS: $permissionSendCheck; READ_SMS: $permissionReadCheck"
        )

        if (permissionSendCheck == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "PPP", "Permission Granted for SEND_SMS")
        } else {
            Log.println(Log.INFO, "PPP", "Permission requesting for ALL_SMS")
            requestPermissions(context as Activity, permissionsNeeded, PERMISSIONS_REQUEST_SMS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.println(
            Log.INFO,
            "PPP",
            "ON_Requesting_SMS : $requestCode : ${permissions.size} : ${grantResults.size}"
        )
        if (requestCode == PERMISSIONS_REQUEST_SMS) {
            for (i in 0..grantResults.size) {
                if (grantResults.size > i) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.println(
                            Log.INFO,
                            "PPP",
                            "User given Permission Granded for ${permissions[i]}"
                        )
                    } else {
                        Log.println(
                            Log.INFO,
                            "PPP",
                            "User reject Permission Granded for ${permissions[i]}"
                        )
                    }
                    val permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i])
                    Log.println(
                        Log.INFO,
                        "PPP",
                        "Permission sms send after: $permissions[i] $permissionCheck"
                    )
                }

            }
        } else {
            Log.println(Log.INFO, "PPP", "Permission code not match: $requestCode")
        }
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts("contacts displaying form onRequestPermission")
            } else {
                Log.println(Log.INFO, "PPP", "You have disabled a contacts permission")
            }
        }
    }

    fun requestContactPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Read Contacts permission")
                builder.setPositiveButton(R.string.ok, null)
                builder.setMessage("Please enable access to contacts.")
                builder.setOnDismissListener {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
                builder.show()
            } else {
                requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        } else {
            getContacts("getting contacts")
        }
    }

    fun getContacts(message: String) {
        //TODO get contacts code here
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}