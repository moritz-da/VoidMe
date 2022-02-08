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

import java.lang.ref.WeakReference;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class SaveEntryTask extends AsyncTask<String, Integer, Enum<SaveEntryTask.SaveEntryResponse>> {

    private static final String TAG = "-SAVE_ENTRY_TASK-";
    private final WeakReference<Activity> mActivity;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    protected enum SaveEntryResponse {
        SUCCESSFUL,
        ALREADY_EXISTS,
        NO_LOCATION
    }

    public SaveEntryTask(Activity activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity.get());
        mLocationRequest = LocationRequest.create();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity.get());
        if (sharedPreferences.getString("gps_precision", "precise").equals("precise")) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d(TAG, "Use gps (high precision)");
        }
        else {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            Log.d(TAG, "Use towers + wifi (low precision)");
        }
    }

    @SuppressLint("MissingPermission")  // has already been checked and granted
    @Override
    protected Enum<SaveEntryResponse> doInBackground(String... strings) {
        String title = strings[0];
        String description = strings[1];
        String category = strings[2];
        int severity = Integer.parseInt(strings[3]);

        if (mLocation == null) {
            mLocation = new Location("location");
        }

        // get current location
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        mFusedLocationProviderClient.getCurrentLocation(mLocationRequest.getPriority(),
                cancellationTokenSource.getToken()).addOnSuccessListener(mActivity.get(), location -> {
                    if (location != null) {
                        mLocation.set(location);
                        Log.d(TAG, "Longitude: " + location.getLongitude());
                        Log.d(TAG, "Latitude: " + location.getLatitude());
                        Log.d(TAG, "Altitude: " + location.getAltitude());
                        Log.d(TAG, "Accuracy: " + location.getAccuracy());
                    }
                });

        // wait for location, but max 10sec
        boolean hasLocation = false;
        try {
            for (int i=0; i<100; i++) {
                Thread.sleep(100);
                if (mLocation.getLongitude() != 0) {
                    hasLocation = true;
                    break;
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!hasLocation) {
            return SaveEntryResponse.NO_LOCATION;
        }

        // save entry in db
        LocationEntity locationEntity = new LocationEntity(
            title,
            description,
            category,
            mLocation.getLatitude(),
            mLocation.getLongitude(),
            mLocation.getAltitude(),
            mLocation.getAccuracy(),
            severity
        );
        if (DbManager.voidLocation.locationDao().insert(locationEntity) == -1) { // normally row id, but -1 if not inserted in db
            return SaveEntryResponse.ALREADY_EXISTS;
        }
        Log.d(TAG, "Complete db: " + DbManager.voidLocation.locationDao().getAll());
        return SaveEntryResponse.SUCCESSFUL;
    }

    @Override
    protected void onPostExecute(Enum<SaveEntryResponse> saveEntryResponseEnum) {
        switch((SaveEntryResponse) saveEntryResponseEnum) {
            case ALREADY_EXISTS:
                Toast.makeText(mActivity.get(), R.string.location_exists, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "...entry not saved! (location already exists)");
                break;
            case NO_LOCATION:
                Toast.makeText(mActivity.get(), R.string.location_not_found, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "...entry not saved! (location not found)");
                break;
            case SUCCESSFUL:
                Toast.makeText(mActivity.get(), R.string.location_saved, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "...entry saved!");
                break;
            default:
                // nothing
        }
    }
}
