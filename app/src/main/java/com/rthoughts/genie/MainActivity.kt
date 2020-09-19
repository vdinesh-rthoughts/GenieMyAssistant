package com.rthoughts.genie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        smsPermissions(this)
        requestContactPermission(this)

        //smsSendPermission(this)
        //smsReceivePermission(this)
        //GeniePermissions().smsReceivePermission(this)
        //GeniePermissions().smsSendPermission(this)
        //GeniePermissions().smsPermissions(this)

        btnIM.setOnClickListener {
            val intent = Intent(this, IdentityMessaging::class.java)
            startActivity(intent)
        }

        //var tv = findViewById<TextView>(R.id.messageText)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
//8610456290