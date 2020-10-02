package com.rthoughts.genie

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirstEspressoTest {

    private val mUiDevice: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRuleSMS: GrantPermissionRule =
        GrantPermissionRule.grant("android.permission.SEND_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.READ_SMS")

    @Rule
    @JvmField
    var mGrantPermissionRuleContact: GrantPermissionRule =
        GrantPermissionRule.grant("android.permission.READ_CONTACTS")

    @Test
    fun aSendSms() {
        onView(withId(R.id.btnIM)).check(matches(isClickable())).perform(click())

        onView(withId(R.id.layout_send_sms)).check(matches(isDisplayed()))

        onView(withText("Send Message")).check(matches(isDisplayed()))

        onView(withId(R.id.editTextPhone)).perform(clearText()).perform(typeText("8124550344"))
        closeSoftKeyboard()

        onView(withId(R.id.editTextTextPersonName)).perform(clearText())
            .perform(typeText("hi Espressso"))
        closeSoftKeyboard()

        onView(allOf(withId(R.id.btnSend), withText("SEND"))).perform(click())
    }

    @Test
    fun checkContact() {
        onView(withId(R.id.btnCheckContact)).check(matches(isClickable())).perform(click())

        onView(withId(R.id.mobileNumber)).check(matches(isDisplayed()))
            .perform(typeText("9787342474"))
        closeSoftKeyboard()

        onView(withId(R.id.btnDisplayName)).check(matches(isClickable())).perform(click())
        mUiDevice.waitForIdle()
        onView(withId(R.id.lblContactDisplayName)).check(matches(withText("Dad")))
    }
}