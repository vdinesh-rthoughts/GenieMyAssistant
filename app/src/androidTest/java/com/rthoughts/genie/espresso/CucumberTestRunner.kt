package com.rthoughts.genie.espresso

import android.os.Bundle
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnitRunner
import cucumber.api.android.CucumberInstrumentationCore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CucumberTestRunner : AndroidJUnitRunner() {

    var cucumberInstrumentationCore = CucumberInstrumentationCore(this)

    override fun onCreate(bundle: Bundle?) {
        cucumberInstrumentationCore.create(bundle)
        super.onCreate(bundle)
    }

    override fun onStart() {
        waitForIdleSync()
        cucumberInstrumentationCore.start()
    }
}