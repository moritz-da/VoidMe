package de.hdmstuttgart.voidme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.SeekBar;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddNewEntryTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @ClassRule
    public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Test
    public void AddNewEntryTest() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.navigation_home)).perform(click());

        onView(withId(R.id.btn_add_location)).perform(click());

        onView(withId(R.id.locationName)).perform(typeText("This is a test"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.locationDescription)).perform(typeText("blablablablabla"));
        Espresso.closeSoftKeyboard();

        // Choose category
        /*
        String selectionText = "Busy Place";
        onView(withId(R.id.categorySelector)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(withId(R.id.categorySelector)).check(matches(withSpinnerText(containsString(selectionText))));
         */

        onView(withId(R.id.severityLevel)).perform(setProgress(3));


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        Espresso.onView(ViewMatchers.withId(R.id.locationDescription)).perform(customSwipeUp());
//        Espresso.onView(allOf(ViewMatchers.withId(R.id.locationDescription), ViewMatchers.isDisplayingAtLeast(1))).perform(customSwipeUp());

        onView(withId(R.id.saveNewLocation)).perform(click());



        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    // set progress as a value in SeekBar
    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    public static ViewAction customSwipeUp() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,GeneralLocation.TOP_CENTER, Press.FINGER);}
}