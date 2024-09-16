package com.rthoughts.genie

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.rthoughts.genie.databinding.ActivityMainBinding
import com.rthoughts.genie.sms.SmsAIActivity


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIM.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, SendSMS::class.java)
                startActivity(intent)
            } else {
                smsPermissions(this)
            }
        }
        binding.btnCheckContact.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                displayContactName()
            } else {
                requestContactPermission(this)
            }
        }

        binding.btnAISMS.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, SmsAIActivity::class.java)
                startActivity(intent)
            } else {
                smsPermissions(this)
            }
        }
    }

    private fun displayContactName() {
        binding.ltGetContactName.visibility = View.VISIBLE
        binding.mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                if (cs.isNotEmpty()) {
                    binding.btnDisplayName.isEnabled = true
                }
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun afterTextChanged(arg0: Editable) {
            }
        })

        binding.btnDisplayName.setOnClickListener {
            val number = binding.mobileNumber.text.toString()
            val displayName = getContactDisplayNameByNumber(contentResolver, number)
            binding.lblContactDisplayName.text = displayName
        }
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