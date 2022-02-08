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

    public static List<Address> getAddress(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            Log.w("LocationService", "geocoder object created and is present: " + Geocoder.isPresent());
            Log.d("LocationService", "Geocoder: " + addresses.toString());
        } catch (IOException e) {
            Log.e("LocationService", "LocationService getAddress: ", e);
        }
        return addresses;
    }

    // Feature not implemented yet
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