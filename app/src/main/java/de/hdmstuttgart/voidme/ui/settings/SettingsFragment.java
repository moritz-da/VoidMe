package de.hdmstuttgart.voidme.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.preference.PreferenceFragmentCompat;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.databinding.FragmentNotificationsBinding;

public class SettingsFragment extends PreferenceFragmentCompat {
    //TODO https://www.youtube.com/watch?v=ojixfk4FPNU
    private FragmentNotificationsBinding binding;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
