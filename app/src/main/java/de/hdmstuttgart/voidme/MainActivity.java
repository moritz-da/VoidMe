package de.hdmstuttgart.voidme;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.databinding.ActivityMainBinding;
import de.hdmstuttgart.voidme.location.LocationService;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "-MAIN-";
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Toolbar toolbar = findViewById(R.id.home_bar);
        //setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).hide();
        //BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_location_list)
                .build();
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        final NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setupSharedPreferences();
        setupPermissions();
        if (DbManager.voidLocation == null) {
            DbManager.initDb(this);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.dark_mode_key))) setTheme(sharedPreferences);

        // TODO: Move this to LocationService and call it before each location request.
        if(key.equals(getString(R.string.gps_precision_key))) {
            if (sharedPreferences.getString("gps_precision", "precise").equals("precise")) {
                LocationService.getInstance().setUseHighPrecision(true);
                Log.d(TAG, "Use gps (high precision)");
            }
            else {
                LocationService.getInstance().setUseHighPrecision(false);
                Log.d(TAG, "Use towers+wifi (lower precision)");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setupPermissions() {
        //TODO
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION );
            }
        }
    }

    private void setTheme(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getBoolean(getString(R.string.dark_mode_key), true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}