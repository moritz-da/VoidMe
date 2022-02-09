package de.hdmstuttgart.voidme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdmstuttgart.voidme.database.DbManager;

@RunWith(AndroidJUnit4.class)
public class AddNewEntryTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    @After
    public void setup() {
        DbManager.voidLocation.locationDao().deleteAll();
    }

    /**
     *  We test whether an entry can be added to the database and whether it is also displayed in the list
     */
    @Test
    public void addNewEntryTest() {

        onView(withId(R.id.btn_add_location)).perform(click());

        onView(withId(R.id.locationName)).perform(typeText("This is a test"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.locationDescription)).perform(typeText("blablablablabla"));
        Espresso.closeSoftKeyboard();

        // choose category
        /*
        String selectionText = "Busy Place";
        onView(withId(R.id.categorySelector)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(withId(R.id.categorySelector)).check(matches(withSpinnerText(containsString(selectionText))));
         */

        onView(withId(R.id.severityLevel)).perform(setProgress(3));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.saveNewLocation)).perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.navigation_location_list)).perform(click());

        onView(withId(R.id.locationListRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("This is a test")))));
        onView(withId(R.id.locationListRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("blablablablabla")))));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the value from Seekbar
     *
     * @param progress In our case it should be between 0 and 3.
     */
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

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}