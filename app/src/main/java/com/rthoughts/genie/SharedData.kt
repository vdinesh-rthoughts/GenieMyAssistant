package com.rthoughts.genie

class SharedData {
    companion object {
        var smsAIFlag = false;
        var forwardOriginalMessage = false;
        var forwardForwardedMessage = false;
        lateinit var receiver: String
    }
}