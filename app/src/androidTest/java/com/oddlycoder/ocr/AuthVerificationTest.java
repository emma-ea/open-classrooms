package com.oddlycoder.ocr;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.oddlycoder.ocr.views.AuthActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AuthVerificationTest {

    private String authCode;

    @Rule
    public ActivityScenarioRule<AuthActivity> activityRule =
            new ActivityScenarioRule<>(AuthActivity.class);

    @Before
    public void enter_authCode() {
        authCode = "1223";
    }

    @Test
    public void verify_authCode_from_editText() {
        Espresso.onView(ViewMatchers.withId(R.id.auth_code_edit_text))
                .perform(ViewActions.typeText(authCode), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.button_auth_user))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.auth_code_edit_text))
                .check(ViewAssertions.matches(ViewMatchers.withText(authCode)));

    }

}
