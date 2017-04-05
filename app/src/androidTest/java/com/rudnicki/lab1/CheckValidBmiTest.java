package com.rudnicki.lab1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CheckValidBmiTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void validBmi() {
        onView(withId(R.id.height_edit_text))
                .perform(typeText("100"));
        onView(withId(R.id.weight_edit_text))
                .perform(typeText("100"));
        onView(withId(R.id.count_button))
                .perform(click());
        onView(withId(R.id.bmi_placeholder))
                .check(matches(withText("100.00")));
    }

}
