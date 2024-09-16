package com.rthoughts.genie.sms

data class SmsData(val senderNumber: String?, val message: String, val date: Long, val receiverNumber: String="8124550344") {}