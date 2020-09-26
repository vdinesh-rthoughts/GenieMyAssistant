package com.rthoughts.contacttest

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val TAG_ANDROID_CONTACTS = "ANDROID_CONTACTS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "dev2qa.com - Android Contacts Operate Example."

        // Load all contacts, and print each contact as log debug info.
        val loadButton: Button = findViewById(R.id.contact_operate_load)
        loadButton.setOnClickListener {
            if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS)) {
                requestPermission(Manifest.permission.READ_CONTACTS)
            } else {
                getAllContacts()
                myMethod()
                Toast.makeText(this@MainActivity,
                    "Contact data has been printed in the android monitor log..",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun myMethod(cursor: Cursor?, dataContentUri: Uri) {
        if (cursor != null) {
            println("$dataContentUri: " + Arrays.toString(cursor.columnNames))
            println("Cursor Size: " + cursor.count)

            if (cursor.moveToFirst()) {
                for (element in cursor.columnNames) {
                    println("MMCC $element : " + cursor.getString(cursor.getColumnIndex(element)))
                }
                //println("MMCC display_name : " + cursor.getString(cursor.getColumnIndex("display_name")))
                //println("MMCC data1 : " + cursor.getString(cursor.getColumnIndex("data1")))
            }
        } else {
            Log.i("SMS", "No message in the device")
        }
        cursor?.close()
    }

    fun myMethod() {
        val dataContentUri: Uri = ContactsContract.Data.CONTENT_URI
        val whareClause =
            "mimetype='vnd.android.cursor.item/phone_v2' and replace(data1,' ','') like '%812450344%'"
        val cursor: Cursor? = contentResolver.query(dataContentUri, null, whareClause, null, null)
        if (cursor != null) {
            println("$dataContentUri: " + Arrays.toString(cursor.columnNames))
            println("Record count: " + cursor.count)

            var rowCount = 0
            if (cursor.moveToFirst()) {
                println("display_name DN: " + cursor.getString(cursor.getColumnIndex("display_name")))
                do {
                    var row = ""
                    for (element in cursor.columnNames) {
                        val pair: String =
                            "$element : " + cursor.getString(cursor.getColumnIndex(element))
                        row = row + "\t" + pair
                    }
                    println("Record count: ${++rowCount}" + row)
                } while (cursor.moveToNext())
            } else {
                Log.i("CONTACT", "No contact found in the device")

            }
        } else {
            Log.i("SMS", "No contact found in the device")
        }
        cursor?.close()
    }

    /* Return all contacts and show each contact data in android monitor console as debug info. */
    private fun getAllContacts(): List<ContactDTO>? {
        val ret: List<ContactDTO> = ArrayList()

        // Get all raw contacts id list.
        val rawContactsIdList = getRawContactsIdList()
        val contactListSize = rawContactsIdList.size
        val contentResolver = contentResolver

        // Loop in the raw contacts list.
        for (i in 0 until contactListSize) {
            // Get the raw contact id.
            val rawContactId = rawContactsIdList[i]
            Log.d(TAG_ANDROID_CONTACTS, "raw contact id : $rawContactId")

            // Data content uri (access data table. )
            val dataContentUri: Uri = ContactsContract.Data.CONTENT_URI

            // Build query columns name array.
            val queryColumnList: MutableList<String> = ArrayList()

            // ContactsContract.Data.CONTACT_ID = "contact_id";
            queryColumnList.add(ContactsContract.Data.CONTACT_ID)

            // ContactsContract.Data.MIMETYPE = "mimetype";
            queryColumnList.add(ContactsContract.Data.MIMETYPE)
            queryColumnList.add(ContactsContract.Data.DATA1)
            queryColumnList.add(ContactsContract.Data.DATA2)
            queryColumnList.add(ContactsContract.Data.DATA3)
            queryColumnList.add(ContactsContract.Data.DATA4)
            queryColumnList.add(ContactsContract.Data.DATA5)
            queryColumnList.add(ContactsContract.Data.DATA6)
            queryColumnList.add(ContactsContract.Data.DATA7)
            queryColumnList.add(ContactsContract.Data.DATA8)
            queryColumnList.add(ContactsContract.Data.DATA9)
            queryColumnList.add(ContactsContract.Data.DATA10)
            queryColumnList.add(ContactsContract.Data.DATA11)
            queryColumnList.add(ContactsContract.Data.DATA12)
            queryColumnList.add(ContactsContract.Data.DATA13)
            queryColumnList.add(ContactsContract.Data.DATA14)
            queryColumnList.add(ContactsContract.Data.DATA15)

            // Translate column name list to array.
            val queryColumnArr = queryColumnList.toTypedArray()

            // Build query condition string. Query rows by contact id.
            val whereClauseBuf = StringBuffer()
            whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID)
            whereClauseBuf.append("=")
            whereClauseBuf.append(rawContactId)

            // Query data table and return related contact data.
            Log.i("QUERY PARA URI: ", dataContentUri.toString())
            Log.i("QUERY PARA Columns: ", queryColumnArr.contentToString())
            Log.i("QUERY PARA WhereClause: ", whereClauseBuf.toString())

            val cursor: Cursor? = contentResolver.query(dataContentUri,
                queryColumnArr,
                whereClauseBuf.toString(),
                null,
                null)
            myMethod()

            /* If this cursor return database table row data.
               If do not check cursor.getCount() then it will throw error
               android.database.CursorIndexOutOfBoundsException: Index 0 requested, with a size of 0.
               */if (cursor != null && cursor.count > 0) {
                val lineBuf = StringBuffer()
                cursor.moveToFirst()
                lineBuf.append("Raw Contact Id : ")
                lineBuf.append(rawContactId)
                val contactId: Long =
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID))
                lineBuf.append(" , Contact Id : ")
                lineBuf.append(contactId)
                do {
                    // First get mimetype column value.
                    val mimeType: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                    lineBuf.append(" \r\n , MimeType : ")
                    lineBuf.append(mimeType)
                    val dataValueList = getColumnValueByMimetype(cursor, mimeType)
                    val dataValueListSize = dataValueList.size
                    for (j in 0 until dataValueListSize) {
                        val dataValue = dataValueList[j]
                        lineBuf.append(" , ")
                        lineBuf.append(dataValue)
                    }
                } while (cursor.moveToNext())
                Log.d(TAG_ANDROID_CONTACTS, lineBuf.toString())
            }
            Log.d(TAG_ANDROID_CONTACTS,
                "=========================================================================")
        }
        return ret
    }

    /*
     *  Get email type related string format value.
     * */
    private fun getEmailTypeString(dataType: Int): String {
        var ret = ""
        if (ContactsContract.CommonDataKinds.Email.TYPE_HOME == dataType) {
            ret = "Home"
        } else if (ContactsContract.CommonDataKinds.Email.TYPE_WORK == dataType) {
            ret = "Work"
        }
        return ret
    }

    /*
     *  Get phone type related string format value.
     * */
    private fun getPhoneTypeString(dataType: Int): String {
        var ret = ""
        when {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME == dataType -> {
                ret = "Home"
            }
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK == dataType -> {
                ret = "Work"
            }
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE == dataType -> {
                ret = "Mobile"
            }
        }
        return ret
    }

    /*
    *  Return data column value by mimetype column value.
    *  Because for each mimetype there has not only one related value,
    *  such as Organization.CONTENT_ITEM_TYPE need return company, department, title, job description etc.
    *  So the return is a list string, each string for one column value.
    * */
    private fun getColumnValueByMimetype(cursor: Cursor, mimeType: String): List<String> {
        val ret: MutableList<String> = ArrayList()
        when (mimeType) {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                // Email.ADDRESS == data1
                val emailAddress: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                // Email.TYPE == data2
                val emailType: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
                val emailTypeStr = getEmailTypeString(emailType)
                ret.add("Email Address : $emailAddress")
                ret.add("Email Int Type : $emailType")
                ret.add("Email String Type : $emailTypeStr")
            }
            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE -> {
                // Im.PROTOCOL == data5
                val imProtocol: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL))
                // Im.DATA == data1
                val imId: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA))
                ret.add("IM Protocol : $imProtocol")
                ret.add("IM ID : $imId")
            }
            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE -> {
                // Nickname.NAME == data1
                val nickName: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME))
                ret.add("Nick name : $nickName")
            }
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE -> {
                // Organization.COMPANY == data1
                val company: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
                // Organization.DEPARTMENT == data5
                val department: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT))
                // Organization.TITLE == data4
                val title: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE))
                // Organization.JOB_DESCRIPTION == data6
                val jobDescription: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.JOB_DESCRIPTION))
                // Organization.OFFICE_LOCATION == data9
                val officeLocation: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.OFFICE_LOCATION))
                ret.add("Company : $company")
                ret.add("department : $department")
                ret.add("Title : $title")
                ret.add("Job Description : $jobDescription")
                ret.add("Office Location : $officeLocation")
            }
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                // Phone.NUMBER == data1
                val phoneNumber: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                // Phone.TYPE == data2
                val phoneTypeInt: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                val phoneTypeStr = getPhoneTypeString(phoneTypeInt)
                ret.add("Phone Number : $phoneNumber")
                ret.add("Phone Type Integer : $phoneTypeInt")
                ret.add("Phone Type String : $phoneTypeStr")
            }
            ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE -> {
                // SipAddress.SIP_ADDRESS == data1
                val address: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS))
                // SipAddress.TYPE == data2
                val addressTypeInt: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.SipAddress.TYPE))
                val addressTypeStr = getEmailTypeString(addressTypeInt)
                ret.add("Address : $address")
                ret.add("Address Type Integer : $addressTypeInt")
                ret.add("Address Type String : $addressTypeStr")
            }
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE -> {
                // StructuredName.DISPLAY_NAME == data1
                val displayName: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME))
                // StructuredName.GIVEN_NAME == data2
                val givenName: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                // StructuredName.FAMILY_NAME == data3
                val familyName: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                ret.add("Display Name : $displayName")
                ret.add("Given Name : $givenName")
                ret.add("Family Name : $familyName")
            }
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE -> {
                // StructuredPostal.COUNTRY == data10
                val country: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY))
                // StructuredPostal.CITY == data7
                val city: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY))
                // StructuredPostal.REGION == data8
                val region: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION))
                // StructuredPostal.STREET == data4
                val street: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET))
                // StructuredPostal.POSTCODE == data9
                val postcode: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE))
                // StructuredPostal.TYPE == data2
                val postType: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE))
                val postTypeStr = getEmailTypeString(postType)
                ret.add("Country : $country")
                ret.add("City : $city")
                ret.add("Region : $region")
                ret.add("Street : $street")
                ret.add("Postcode : $postcode")
                ret.add("Post Type Integer : $postType")
                ret.add("Post Type String : $postTypeStr")
            }
            ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE -> {
                // Identity.IDENTITY == data1
                val identity: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.IDENTITY))
                // Identity.NAMESPACE == data2
                val namespace: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.NAMESPACE))
                ret.add("Identity : $identity")
                ret.add("Identity Namespace : $namespace")
            }
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE -> {
                // Photo.PHOTO == data15
                val photo: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
                // Photo.PHOTO_FILE_ID == data14
                val photoFileId: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_FILE_ID))
                ret.add("Photo : $photo")
                ret.add("Photo File Id: $photoFileId")
            }
            ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE -> {
                // GroupMembership.GROUP_ROW_ID == data1
                val groupId: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID))
                ret.add("Group ID : $groupId")
            }
            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE -> {
                // Website.URL == data1
                val websiteUrl: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL))
                // Website.TYPE == data2
                val websiteTypeInt: Int =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE))
                val websiteTypeStr = getEmailTypeString(websiteTypeInt)
                ret.add("Website Url : $websiteUrl")
                ret.add("Website Type Integer : $websiteTypeInt")
                ret.add("Website Type String : $websiteTypeStr")
            }
            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE -> {
                // Note.NOTE == data1
                val note: String? =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))
                ret.add("Note : $note")
            }
        }
        return ret
    }

    // Return all raw_contacts _id in a list.
    private fun getRawContactsIdList(): List<Int> {
        val ret: MutableList<Int> = ArrayList()
        val contentResolver = contentResolver

        // Row contacts content uri( access raw_contacts table. ).
        val rawContactUri: Uri = ContactsContract.RawContacts.CONTENT_URI
        // Return _id column in contacts raw_contacts table.
        val queryColumnArr = arrayOf(ContactsContract.RawContacts._ID)
        // Query raw_contacts table and return raw_contacts table _id.
        val cursor: Cursor? = contentResolver.query(rawContactUri, queryColumnArr, null, null, null)
        myMethod(contentResolver.query(rawContactUri, queryColumnArr, null, null, null),
            rawContactUri)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val idColumnIndex: Int = cursor.getColumnIndex(ContactsContract.RawContacts._ID)
                val rawContactsId: Int = cursor.getInt(idColumnIndex)
                ret.add(rawContactsId)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return ret
    }


    // Check whether user has phone contacts manipulation permission or not.
    private fun hasPhoneContactsPermission(permission: String): Boolean {
        var ret = false

        // If android sdk version is bigger than 23 the need to check run time permission.

        // return phone read contacts permission grant status.
        val hasPermission = ContextCompat.checkSelfPermission(applicationContext, permission)
        // If permission is granted then return true.
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            ret = true
        }
        return ret
    }

    // Request a runtime permission to app user.
    private fun requestPermission(permission: String) {
        val requestPermissionArray = arrayOf(permission)
        ActivityCompat.requestPermissions(this, requestPermissionArray, 1)
    }

    // After user select Allow or Deny button in request runtime permission dialog
    // , this method will be invoked.
    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val length = grantResults.size
        if (length > 0) {
            val grantResult = grantResults[0]
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext,
                    "You allowed permission, please click the button again.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "You denied permission.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
