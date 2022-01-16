package de.hdmstuttgart.voidme.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import de.hdmstuttgart.voidme.R;

public class SettingsDetailFragment extends PreferenceFragment implements ISettingsFragment {

    private static final String TAG = "-PREF-";
    public static String FRAGMENT_TAG;

    //private FragmentNotificationsBinding binding;
    //private SharedPreferences sharedPreferences;

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    /**
     * <strong>Factory method that sets attributes and builds dynamically new SettingsDetailFragments.</strong>
     * Fragments are created depending on their preference key of the PreferenceScreen to use as the
     * root of the preference hierarchy that is going to be loaded.
     * @param preferenceScreen A container that represents the settings screen or a preferences sub-screen.
     * @return Instance of SettingsDetailFragment.
     */
    public static SettingsDetailFragment newInstance(@NonNull PreferenceScreen preferenceScreen) {
        SettingsDetailFragment fragment = new SettingsDetailFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        Log.d(TAG, "new detail pref instance: " + preferenceScreen.getKey());
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null) {
            FRAGMENT_TAG = getArguments().getString(ARG_PREFERENCE_ROOT);
            Log.d(TAG, "onCreateView: " + FRAGMENT_TAG);
        } else {
            Log.e(TAG, "onCreateView: Args null");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setPreferencesFromResource(R.xml.root_preferences, FRAGMENT_TAG);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //setPreferencesFromResource(R.xml.root_preferences, FRAGMENT_TAG);
        Log.d(TAG, "onCreatePreferences of the sub screen " + rootKey);
    }
}