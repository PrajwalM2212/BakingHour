package com.bhavaneulergmail.baking;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void detailsActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.name_recycler_view),
                        withParent(allOf(withId(R.id.nameFrag),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.measure), withText("Measure : G"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredient_recycler_view),
                                        1),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Measure : G")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.measure), withText("Measure : TSP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredient_recycler_view),
                                        2),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Measure : TSP")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.ingredient), withText("Ingredient : sifted cake flour"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredient_recycler_view),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Ingredient : sifted cake flour")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.ingredient), withText("Ingredient : sifted cake flour"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredient_recycler_view),
                                        0),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("Ingredient : sifted cake flour")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
