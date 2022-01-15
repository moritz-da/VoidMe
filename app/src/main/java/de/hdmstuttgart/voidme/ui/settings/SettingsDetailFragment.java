package de.hdmstuttgart.voidme.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.databinding.FragmentNotificationsBinding;

public class SettingsDetailFragment extends SettingsFragment {

    public static String FRAGMENT_TAG;

    private FragmentNotificationsBinding binding;
    private SharedPreferences sharedPreferences;

    public static Fragment getInstance(@NonNull PreferenceScreen preferenceScreen) {
        SettingsDetailFragment fragment = new SettingsDetailFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getArguments() != null) {
            FRAGMENT_TAG = getArguments().getString(ARG_PREFERENCE_ROOT);
        }

        //TODO update action bar text & back button

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, FRAGMENT_TAG);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireActivity());
    }
}