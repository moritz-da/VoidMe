package de.hdmstuttgart.voidme.shared.utils.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class LocationService {

    private LocationService() {
        // not called
    }

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

    public static Location getLocation(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        Location location = new Location("location");
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                location.setLongitude(addresses.get(0).getLongitude());
                location.setLatitude(addresses.get(0).getLatitude());
            }
        } catch (IOException e) {
            Log.e("LocationService", "LocationService getLocation: ", e);
        }
        return location;
    }
}