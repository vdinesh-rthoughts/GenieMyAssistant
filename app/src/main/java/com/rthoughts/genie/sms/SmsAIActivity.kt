package com.rthoughts.genie.sms

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rthoughts.genie.R
import com.rthoughts.genie.SharedData
import com.rthoughts.genie.databinding.ActivitySmsAiBinding

class SmsAIActivity : AppCompatActivity() {

    lateinit var binding: ActivitySmsAiBinding
    lateinit var selectRadio: RadioButton
    lateinit var selectedNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsAiBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnReceiver.setOnClickListener {
            val selected = binding.forwardList.checkedRadioButtonId
            selectRadio = findViewById(selected)
            selectedNumber = selectRadio.hint.toString()
            SharedData.receiver=selectedNumber
            SharedData.smsAIFlag=true
        }
    }
}