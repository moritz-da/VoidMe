package de.hdmstuttgart.voidme.shared.utils.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class SaveEntryTask extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "-SAVE_ENTRY_TASK-";
    //private static final int DEFAULT_UPDATE_INTERVAL = 30;
    //private static final int FAST_UPDATE_INTERVAL = 5;
    private Activity activity;
    private Location locationTemp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    public SaveEntryTask(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        locationRequest = LocationRequest.create();
        //locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        //locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (sharedPreferences.getString("gps_precision", "precise").equals("precise")) {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d(TAG, "Use gps (high precision)");
        }
        else {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            Log.d(TAG, "Use towers+wifi (lower precision)");
        }
    }

    @SuppressLint("MissingPermission")  // has already been proved and granted
    @Override
    protected Boolean doInBackground(String... strings) {
        String title = strings[0];
        String description = strings[1];
        String category = strings[2];
        int severity = Integer.parseInt(strings[3]);

        if (locationTemp == null) {
            locationTemp = new Location("location");
        }

        // get current position
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        fusedLocationProviderClient.getCurrentLocation(locationRequest.getPriority(),
                cancellationTokenSource.getToken()).addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locationTemp.set(location);
                    Log.d(TAG, "Lon: " + location.getLongitude());
                    Log.d(TAG, "Lat: " + location.getLatitude());
                    Log.d(TAG, "Alt: " + location.getAltitude());
                    Log.d(TAG, "Acc: " + location.getAccuracy());
                }
            }
        });

        // save entry in db
        while (locationTemp.getLongitude() == 0) {
            // wait for location...
        }

        LocationEntity locationEntity = new LocationEntity(
            title,
            description,
            category,
            locationTemp.getLatitude(),
            locationTemp.getLongitude(),
            locationTemp.getAltitude(),
            locationTemp.getAccuracy(),
            severity
        );

        // normally row id, but -1 if not inserted in db
        if (DbManager.voidLocation.locationDao().insert(locationEntity) == -1) {
            //SQLiteConstraintException, Location already exists!
            //TODO: Output of a more detailed error description
            Log.w(TAG, "Location already exists!");
            return false;
        }
        Log.d(TAG, "DB: " + DbManager.voidLocation.locationDao().getAll());
        return true;
    }

    @Override
    protected void onPostExecute(Boolean wasSuccessful) {
        if(wasSuccessful) {
            Toast.makeText(activity, R.string.saved_new_location, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "...entry saved!");
        }
        else {
            Toast.makeText(activity, R.string.not_saved_new_location, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "...entry not saved!");
        }
    }
}
