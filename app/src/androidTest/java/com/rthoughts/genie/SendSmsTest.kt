package com.rthoughts.genie


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SendSmsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule = GrantPermissionRule.grant("android.permission.SEND_SMS",
        "android.permission.RECEIVE_SMS",
        "android.permission.READ_SMS")

    @Test
    fun sendSmsTest() {
        val appCompatButton = onView(allOf(withId(R.id.btnIM),
            withText("Send Message"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
            isDisplayed()))
        appCompatButton.perform(click())

        val appCompatButton2 = onView(allOf(withId(R.id.btnIM),
            withText("Send Message"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
            isDisplayed()))
        appCompatButton2.perform(click())

        val appCompatEditText = onView(allOf(withId(R.id.editTextPhone),
            withText("8124550344"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
            isDisplayed()))
        appCompatEditText.perform(replaceText("81245503446"))

        val appCompatEditText2 = onView(allOf(withId(R.id.editTextPhone),
            withText("81245503446"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
            isDisplayed()))
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(allOf(withId(R.id.editTextMessage),
            withText("Genie send time"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
            isDisplayed()))
        appCompatEditText3.perform(replaceText("Genie send timeg"))

        val appCompatEditText4 = onView(allOf(withId(R.id.editTextMessage),
            withText("Genie send timeg"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
            isDisplayed()))
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatButton3 = onView(allOf(withId(R.id.btnSend),
            withText("Send"),
            childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
            isDisplayed()))
        appCompatButton3.perform(click())
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
                    position)
            }
        }
    }
}
