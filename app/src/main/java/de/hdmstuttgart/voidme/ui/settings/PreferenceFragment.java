package de.hdmstuttgart.voidme.ui.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Abstract class defines base settings of preference fragment screens.
 */
public abstract class PreferenceFragment extends PreferenceFragmentCompat implements ISettingsFragment{
    public abstract String getFragmentTag();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setting default theme background in the view so as to avoid transparency
        TypedValue typedValue = new TypedValue ();
        view.getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        view.setBackgroundColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & typedValue.data))));
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentActionbarTitle();
    }

    private void setCurrentActionbarTitle() {
        // Set actionbar title
        Preference preference = this.findPreference(getFragmentTag());
        String actionBarTitle = "";
        if (preference != null) {
            actionBarTitle = preference.getTitle().toString();
        }
        ((SettingsActivity) requireActivity()).setActionBarTitle(actionBarTitle);
    }

    @Override
    public void onStart() {
        super.onStart();
        setCurrentActionbarTitle();
    }
}