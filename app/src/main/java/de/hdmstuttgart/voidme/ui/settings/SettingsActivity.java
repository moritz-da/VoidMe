package de.hdmstuttgart.voidme.ui.settings;


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

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //create fragment only on first call
        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                Log.e("- PREF -", "Fragment not found by tag.");
                fragment = new SettingsFragment();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.settings, fragment, SettingsFragment.FRAGMENT_TAG);
            ft.commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, @NonNull PreferenceScreen preferenceScreen) {
        //Toast.makeText(this, "prefStartScreen Target: " + preferenceScreen.getKey(), Toast.LENGTH_SHORT).show();
        //TODO Bug: need to press twice to switch
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.settings, SettingsDetailFragment.getInstance(preferenceScreen), preferenceScreen.getKey())
                .addToBackStack(preferenceScreen.getKey())
                .commit();
        return true;
    }
}