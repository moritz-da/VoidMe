package de.hdmstuttgart.voidme.ui.settings;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.ui.di.Helper;

/**
 * Manage preferences
 */
public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private static final String TAG = "-PREF-";
    private ActionBar actionBar;
    private int unlockCount = 5;
    public static Toast mToast;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DEV_UNLOCK = "devUnlock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //create fragment only on first call
        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(PreferencesFragment.FRAGMENT_TAG);
            if (fragment == null) {
                Log.d(TAG, "Fragment not found by tag, creating new.");
                fragment = new PreferencesFragment();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.settings, fragment, PreferencesFragment.FRAGMENT_TAG);
            ft.commit();
        }
        //set action bar up btn to navigate back through all preference screens
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onPreferenceStartScreen(@NonNull PreferenceFragmentCompat caller, @NonNull PreferenceScreen preferenceScreen) {
        Log.d(TAG, "onPreferenceStartScreen: " + preferenceScreen.getKey());
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(DEV_UNLOCK, false)) {

            if (preferenceScreen.getKey().equals(getString(R.string.developer_key)) && unlockCount >= 0) {
                if (unlockCount == 0) {
                    Helper.showToast(mToast,"Unlocked!", Toast.LENGTH_SHORT, this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(DEV_UNLOCK, true);
                    editor.apply();
                } else {
                    Helper.showToast(mToast,"Unlocked in " + unlockCount, Toast.LENGTH_SHORT, this);
                    unlockCount--;
                    return false;
                }
            }
        }
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(preferenceScreen.getKey())
                .replace(R.id.settings, PreferencesDetailFragment.newInstance(preferenceScreen), preferenceScreen.getKey())
                .commit();
        return true;
    }

    /**
     * Sets the actionbar title of this activity to the given value.
     *
     * @param title The desired title.
     */
    void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    /**
     * On actionbar up-button popping fragments from stack until it is empty.
     *
     * @return true if fragment popped or returned to parent activity successfully.
     */
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp " + getSupportFragmentManager().getBackStackEntryCount());
        //Pop back stack if the up button is pressed.
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        if (canGoBack) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
            return super.onSupportNavigateUp();
        }
        return true;
    }
}