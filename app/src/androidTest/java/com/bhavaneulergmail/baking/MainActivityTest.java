package com.bhavaneulergmail.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by prajwalm on 01/10/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test

    public void mainActivityTest(){

        onView(withId(R.id.name_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.title_recipe)).check(matches(withText("Nutella Pie")));

    }


    @Test
    public void testTwo(){

        onView(withId(R.id.name_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
        onView(withId(R.id.title_recipe)).check(matches(withText("Cheesecake")));


    }


    @Test
    public void testThree(){


        onView(withId(R.id.name_recycler_view)).check(matches(hasDescendant(withText("Nutella Pie"))));

        onView(withId(R.id.name_recycler_view)).check(matches(hasDescendant(withText("Cheesecake"))));

        onView(withId(R.id.name_recycler_view)).check(matches(hasDescendant(withText("Brownies"))));

        onView(withId(R.id.name_recycler_view)).check(matches(hasDescendant(withText("Yellow Cake"))));

    }



}
