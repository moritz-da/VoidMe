package de.hdmstuttgart.voidme.shared.utils.location;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;
import de.hdmstuttgart.voidme.shared.exceptions.PermissionDeniedException;

public class SaveEntryTask extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "-SAVE_ENTRY_TASK-";
    public static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FAST_UPDATE_INTERVAL = 5;
    private boolean useHighPrecision = true;

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

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        // TODO: Get information from settings (sharedPreferences)
        if (useHighPrecision) {
            // most accurate -> use GPS
            locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        } else {
            // towers and wifi
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
    }

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
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permissions granted
            try {
                fusedLocationProviderClient.getCurrentLocation(locationRequest.getPriority(), cancellationTokenSource.getToken()).addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            locationTemp.set(location);
                            Log.d(TAG, "Lon: " + location.getLongitude());
                            Log.d(TAG, "Lat: " + location.getLatitude());
                            Log.d(TAG, "Alt: " + location.getAltitude());
                            Log.d(TAG, "Acc: " + location.getAccuracy());
                            Log.d(TAG, "Precision high? " + useHighPrecision);
                        }
                    }
                });

                // save in DB


                // really necessary?
                while (locationTemp.getLongitude() == 0) {
                    // wait...
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
                DbManager.voidLocation.locationDao().insert(locationEntity);
            }
            // TODO: Still a problem with the ConstraintException
            catch (SQLiteConstraintException ex) {
                Log.e(TAG, "Entry not saved!", ex);
                Toast.makeText(activity, R.string.location_exists, Toast.LENGTH_LONG).show();
                return false;
            }
            catch (PermissionDeniedException permEx) {
                Log.e(TAG, "Entry not saved!", permEx);
                Toast.makeText(activity, R.string.location_permission_denied, Toast.LENGTH_LONG).show();
                // TODO request permission
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
                return false;
            }

            Log.d(TAG, "DB: " + DbManager.voidLocation.locationDao().getAll());

            return true;
        }
        else {
            Log.e(TAG, "Can't get location!");
            // permissions not granted yet
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
            // TODO: Is this exception needed?
            //throw new PermissionDeniedException("User denied location permission");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean wasSuccessful) {
        if(wasSuccessful) {
            Toast.makeText(activity, R.string.saved_new_location, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "location saved");
        }
        else {
            Toast.makeText(activity, R.string.not_saved_new_location, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "location not saved");
        }
    }
}
