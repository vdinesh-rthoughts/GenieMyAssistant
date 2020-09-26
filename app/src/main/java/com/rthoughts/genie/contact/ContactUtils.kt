package com.rthoughts.genie.contact

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.rthoughts.genie.BaseActivity
import java.lang.Exception


class ContactUtils : BaseActivity() {

/*    fun getContactDisplayNameByNumber(mobileNumber: String?): String? {
        var displayName = "NOT_FOUND"
        val dataContentUri: Uri = ContactsContract.Data.CONTENT_URI
        mobileNumber?.replace("+91", "")
        val whareClause =
            "mimetype='vnd.android.cursor.item/phone_v2' and replace(data1,' ','') like '%$mobileNumber%'"
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(dataContentUri, null, whareClause, null, null)
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
    }*/
}