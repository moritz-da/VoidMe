package de.hdmstuttgart.voidme.shared.utils.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService {

    public static List<Address> getAddress(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            for (int i = 0; i < 50; i++) {
                addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Log.w("LocationService", "geocoder object created and is present: " + Geocoder.isPresent());
                Log.d("LocationService", "Geocoder: " + addresses.toString());
                if (addresses.size() > 0) break;
            }
        } catch (IOException e) {
            Log.e("LocationService", "LocationService getAddress: ", e);
        }
        return addresses;
    }


/*
    private static LocationService instance;

    private static final String TAG = "-HOME-";
    public static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FAST_UPDATE_INTERVAL = 5;
    private boolean useHighPrecision = true;

    private Activity activityTemp;
    private Location locationTemp;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;


    private LocationService() {
    }

    public static LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    private void setup() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activityTemp);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        // TODO: Get information from settings
        if (useHighPrecision) {
            // most accurate -> use GPS
            locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        } else {
            // towers and wifi
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
    }

    private void locationRequest() throws PermissionDeniedException {
        setup();

        if (locationTemp == null) {
            locationTemp = new Location("location");
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        if (ActivityCompat.checkSelfPermission(activityTemp, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permissions granted
            fusedLocationProviderClient.getCurrentLocation(locationRequest.getPriority(), cancellationTokenSource.getToken()).addOnSuccessListener(activityTemp, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        locationTemp.set(location);
                        Log.d(TAG, "location saved");
                        // second system statement
                        Log.d(TAG, "2. Lon: " + location.getLongitude());
                        Log.d(TAG, "2. Lat: " + location.getLatitude());
                        Log.d(TAG, "2. Alt: " + location.getAltitude());
                        Log.d(TAG, "2. Acc: " + location.getAccuracy());
                        Log.d(TAG, "2. precision high? " + useHighPrecision);
                    }
                }
            });
        } else {
            Log.e(TAG, "Can not get location!");
            // permissions not granted yet
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                activityTemp.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
            throw new PermissionDeniedException("User denied location permission");
        }
    }

    public Location getLocation(Activity activity) throws PermissionDeniedException {
        Log.d(TAG, "getLocation called");
        activityTemp = activity;
        locationRequest();
        return locationTemp;
    }

    public void setUseHighPrecision(boolean useHighPrecision) {
        this.useHighPrecision = useHighPrecision;
    }*/
}

