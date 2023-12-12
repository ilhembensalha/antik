package com.example.antik;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.antik", appContext.getPackageName());
    }
    // Use the ActivityScenarioRule to launch the activity under test
    @Rule
    public ActivityScenarioRule<Signup> activityRule =
            new ActivityScenarioRule<>(Signup.class);

    @Test
    public void testSignupSuccess() {
        // Enter valid data and click the signup button
        Espresso.onView(ViewMatchers.withId(R.id.signup_email))
                .perform(ViewActions.typeText("valid@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.signup_password))
                .perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmpassword))
                .perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.signup_button))
                .perform(ViewActions.click());

        // Check if the expected activity is launched after successful signup
        Espresso.onView(ViewMatchers.withId(R.id.signin))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSignupFailure() {
        // Enter invalid data and click the signup button
        Espresso.onView(ViewMatchers.withId(R.id.signup_email))
                .perform(ViewActions.typeText("invalid-email"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.signup_password))
                .perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmpassword))
                .perform(ViewActions.typeText("differentpassword"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.signup_button))
                .perform(ViewActions.click());

        // Check if an error message is displayed
        Espresso.onView(ViewMatchers.withText("Email and password cannot be empty and password equals confirm password"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}