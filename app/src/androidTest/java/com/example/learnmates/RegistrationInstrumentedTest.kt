package com.example.learnmates

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.learnmates.ui.activity.SignupActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTest {
    @get:Rule
    val testRule = ActivityScenarioRule(SignupActivity::class.java)
    @Test
    fun checkRegistration() {

        onView(withId(R.id.editTextFullName)).perform(
            typeText("Annie")
        )

        onView(withId(R.id.editTextUsername)).perform(
            typeText("Frank")
        )

        onView(withId(R.id.editTextEmail)).perform(
            typeText("annie123@gmail.com")
        )

        onView(withId(R.id.editTextPassword)).perform(
            typeText("Annie123")
        )

        closeSoftKeyboard()

        onView(withId(R.id.termsCheckbox)).perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.signUpBtn)).perform(
            click()
        )

        Thread.sleep(6000)
        onView(withId(R.id.bottomView)).check(matches(isDisplayed()))
    }

}