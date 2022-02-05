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

public class SaveEntryTask extends AsyncTask<String, Integer, Enum<SaveEntryTask.SaveEntryResponse>> {

    private static final String TAG = "-SAVE_ENTRY_TASK-";
    //private static final int DEFAULT_UPDATE_INTERVAL = 30;
    //private static final int FAST_UPDATE_INTERVAL = 5;
    private Activity activity;
    private Location locationTemp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    protected enum SaveEntryResponse {
        SUCCESSFUL,
        ALREADY_EXISTS,
        NO_LOCATION
    }

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
    protected Enum<SaveEntryResponse> doInBackground(String... strings) {
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

        // TODO: Is this the best option?!
        boolean wasSuccessful = false;
        try {
            for (int i = 0; i < 50; i++) {  // wait for location, but max 5sec
                Thread.sleep(100);
                if (locationTemp.getLongitude() != 0) {
                    wasSuccessful = true;
                    break;
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!wasSuccessful) {
            return SaveEntryResponse.NO_LOCATION;
        }
/*
        // save entry in db
        while (locationTemp.getLongitude() == 0) {
            // wait for location...
        }
 */

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
            return SaveEntryResponse.ALREADY_EXISTS;
        }
        Log.d(TAG, "DB: " + DbManager.voidLocation.locationDao().getAll());
        return SaveEntryResponse.SUCCESSFUL;
    }

    @Override
    protected void onPostExecute(Enum<SaveEntryResponse> saveEntryResponseEnum) {
        switch((SaveEntryResponse) saveEntryResponseEnum) {
            case ALREADY_EXISTS:
                Toast.makeText(activity, R.string.location_exists, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "...entry not saved! (location already exists)");
                break;
            case NO_LOCATION:
                Toast.makeText(activity, R.string.location_not_found, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "...entry not saved! (location not found)");
                break;
            case SUCCESSFUL:
                Toast.makeText(activity, R.string.saved_new_location, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "...entry saved!");
                break;
            default:
                // nothing
        }
    }
}
