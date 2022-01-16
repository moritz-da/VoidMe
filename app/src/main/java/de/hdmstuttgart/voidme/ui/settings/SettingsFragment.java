package de.hdmstuttgart.voidme.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import de.hdmstuttgart.voidme.R;

public class SettingsFragment extends PreferenceFragment implements ISettingsFragment{

    public final static String FRAGMENT_TAG = "settings";
    private SharedPreferences sharedPreferences;

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireActivity());
    }
}