package de.hdmstuttgart.voidme.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.concurrent.ThreadLocalRandom;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class SettingsDetailFragment extends PreferenceFragment implements ISettingsFragment, Preference.OnPreferenceClickListener {

    private static final String TAG = "-PREF-";
    public static String FRAGMENT_TAG;

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

    @Override
    public boolean onPreferenceClick (Preference preference) {
        if(preference.getKey().equals(getString(R.string.reset_db_key))) {
            DbManager.voidLocation.locationDao().deleteAll();
            Toast.makeText(getContext(), R.string.reset_db_toast, Toast.LENGTH_SHORT).show();
            return true;
        }
        if(preference.getKey().equals(getString(R.string.db_dummy_key))) {
            int x = 10;
            for (int i = 0; i < x; i++) {
                String[] catArr = getResources().getStringArray(R.array.categories_array);
                LocationEntity dummy = new LocationEntity(
                        "Dummy " + i,
                        "This is the description of Dummy number " + i + " which is a dummy location, suitable for VoidMe.",
                        catArr[ThreadLocalRandom.current().nextInt(0, catArr.length + 1)],
                        (ThreadLocalRandom.current().nextDouble(48.5000, 48.9999 + 1)),
                        ThreadLocalRandom.current().nextDouble(9.0, 9.4000 + 1),
                        0,
                        10,
                        ThreadLocalRandom.current().nextInt(0, 3 + 1)
                );
                DbManager.voidLocation.locationDao().insert(dummy);
            }
            Toast.makeText(getContext(), R.string.db_dummy_toast, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Saving dummy Entries..." + DbManager.voidLocation.locationDao().getAll().toString());
            return true;
        }
        return false;
    }
}