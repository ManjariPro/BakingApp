package com.propelld.app.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.support.test.espresso.contrib.RecyclerViewActions;

@RunWith(AndroidJUnit4.class)
public class MainActivityLoadingBakingsTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource()
    {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void bakingsLoadedInRecycler()
    {
        onView(withId(R.id.receipe)).check(matches(isDisplayed()));
    }

    @Test
    public void bakingsOnClickOpensDescription()
    {
        onView(ViewMatchers.withId(R.id.receipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.details_ingredientHeading)).check(matches(isDisplayed()));
    }

    @Test
    public void bakingsOnClickOpensSteps()
    {
        onView(ViewMatchers.withId(R.id.receipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.details_steps)).check(matches(isDisplayed()));
    }

    @Test
    public void bakingsOnClickStepOpensStepsDetails()
    {
        onView(ViewMatchers.withId(R.id.receipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.details_steps)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.details_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.desc_text)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource()
    {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
