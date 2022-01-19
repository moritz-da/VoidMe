package de.hdmstuttgart.voidme.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import java.util.concurrent.ThreadLocalRandom;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class SettingsFragment extends PreferenceFragment implements ISettingsFragment, Preference.OnPreferenceClickListener {

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

    @Override
    public boolean onPreferenceClick (Preference preference) {
        if(preference.getKey().equals(getString(R.string.reset_db_key))) {
            DbManager.voidLocation.locationDao().deleteAll();
            Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}