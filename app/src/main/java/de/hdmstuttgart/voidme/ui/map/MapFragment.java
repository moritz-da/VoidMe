package de.hdmstuttgart.voidme.ui.map;

import static de.hdmstuttgart.voidme.ui.di.Helper.bitmapDescriptorFromVector;
import static de.hdmstuttgart.voidme.ui.di.Helper.getCategoryIcon;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;
import de.hdmstuttgart.voidme.shared.utils.location.LocationService;
import de.hdmstuttgart.voidme.shared.utils.ui.DrawHelper;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    public List<Marker> markerList = new ArrayList<>();
    public List<CircleOptions> circleList = new ArrayList<>();
    private List<LocationEntity> closeLocations;

    private FusedLocationProviderClient fusedLocationProviderClient;


    private static final LatLng DEFAULT_LOCATION = new LatLng(48.741400, 9.100630);
    private static final int DEFAULT_ZOOM = 14;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    /**
     * Saves map state if fragment on paused.
     *
     * @param outState Bundle in which location and map position are saved.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Stuttgart GERMANY.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map = googleMap;
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoContents(@NonNull Marker marker) {
                    return null;
                }

                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    LocationEntity locationObj = (LocationEntity) marker.getTag();
                    View infoWindow = getLayoutInflater().inflate(R.layout.info_contents, requireView().findViewById(R.id.map), false);
                    TextView title = infoWindow.findViewById(R.id.location_marker_title);
                    title.setText(marker.getTitle());
                    TextView snippet = infoWindow.findViewById(R.id.location_marker_snippet);
                    if (locationObj != null) {
                        List<Address> address = LocationService.getAddress(requireContext(), locationObj.getLatitude(), locationObj.getLongitude());
                        Log.d(TAG, "getInfoWindow: " + address.toString());
                        Log.d(TAG, "getInfoWindow: " + locationObj.getLatitude() + " " + locationObj.getLongitude());
                        snippet.setText(String.format("%s%n%n" + ((address.size() > 0) ? "%s%n%n " : "%s") + "%s %s ",
                                marker.getSnippet(),
                                (address.size() > 0) ? address.get(0).getAddressLine(0) : "",
                                getString(R.string.category), locationObj.getCategory())
                        );
                        ImageView indicator = infoWindow.findViewById(R.id.location_marker_severityIndicator);
                        indicator.setColorFilter(DrawHelper.getColorInt(50f - (locationObj.getSeverity() * 20f)), PorterDuff.Mode.SRC_OVER);
                    } else {
                        snippet.setText(marker.getSnippet());
                    }
                    return infoWindow;
                }
            });

            closeLocations = DbManager.voidLocation.locationDao().getAll();
            for (LocationEntity l : closeLocations) {
                MarkerOptions voidLocation = new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())).title(l.getTitle()).snippet(l.getDescription());
                switch (l.getSeverity()) {
                    case 1:
                        drawCircle(voidLocation.getPosition(), DrawHelper.getColorInt(40f), Math.min(Math.round(l.getAccuracy() * l.getSeverity()), 20));
                        break;
                    case 2:
                        drawCircle(voidLocation.getPosition(), DrawHelper.getColorInt(23f), Math.min(Math.round(l.getAccuracy() * l.getSeverity()), 35));
                        break;
                    case 3:
                        drawCircle(voidLocation.getPosition(), DrawHelper.getColorInt(BitmapDescriptorFactory.HUE_RED), Math.min(Math.round(l.getAccuracy() * l.getSeverity()), 45));
                        break;
                    default:
                        drawCircle(voidLocation.getPosition(), DrawHelper.getColorInt(50f), Math.min(Math.round(l.getAccuracy()), 10));
                }
                voidLocation.icon(bitmapDescriptorFromVector(requireContext(), getCategoryIcon(l.getCategory(), getResources()), l.getSeverity()));
                Marker marker = googleMap.addMarker(voidLocation);
                if (marker != null) marker.setTag(l);
                if (marker != null) marker.setVisible(false);

                markerList.add(marker);
            }
            MarkerOptions newMarker = new MarkerOptions().position(DEFAULT_LOCATION).title("Hochschule der Medien, Stuttgart");
            newMarker.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_voidme_pin));
            googleMap.addMarker(newMarker);

            getLocationPermission();
            if (locationPermissionGranted) {
                updateLocationUI(false);
                getDeviceLocation();
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
            }
            googleMap.setOnCameraMoveListener(() -> {
                for (Marker m : markerList) {
                    m.setVisible(googleMap.getCameraPosition().zoom > 8);
                }
            });
            /*TODO https://developer.android.com/training/location/permissions#background
            if notification for close areas on <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
            https://stackoverflow.com/questions/45747796/android-how-to-show-nearby-user-markers*/
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        updateLocationUI(false);
        if (locationPermissionGranted) {
            getDeviceLocation();
        }
    }

    private void drawCircle(LatLng point, int color, int radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(point)
                .radius(radius)
                .strokeColor(color)
                .strokeWidth(2)
                // Fill color of the circle, set to 40% opacity
                .fillColor(DrawHelper.adjustAlpha(color, 0.4f));
        circleList.add(circleOptions);
        map.addCircle(circleOptions);
    }

    private void updateLocationUI(boolean forceDisabled) {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted && !forceDisabled) {
                //Show blue dot at current position
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %S", e.getMessage());
        }
    }

    private void getLocationPermission() {
        locationPermissionGranted = false;
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            //showInContextUI(...);
            //TODO rational dialog
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Permission request callback handles the response to the permissions dialog.
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                //For multiple Requests like: new ActivityResultContracts.RequestMultiplePermissions(), responseMap -> switch(responseMap) case respMap[0]Permission1: {if respMap[0]BooleanResult then locationPermGranted = true;}
                if (isGranted) {
                    locationPermissionGranted = true;
                    getDeviceLocation();
                } else {
                    //TODO
                    // Explain to the user that the feature is unavailable because the
                    // features require a permission that the user has denied. At the
                    // same time, respect the user's decision.
                }
                updateLocationUI(false);
            });

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM + 2));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        View mapView = inflater.inflate(R.layout.fragment_map, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        return mapView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        //Add location dialog
        /*
        TODO if (addBtn != null) DialogFactory.create(NAME)handleNewLocationEntry(addBtn, view);
        View addBtn = view.findViewById(R.id.saveCurrentLocation);
        if (addBtn != null) {
            //Dialog dialog = DialogFactory.create(DialogFactory.LOCATION_ENTRY_DIALOG, view, getContext(), getActivity());
            //dialog.locationEntryHandler();
        }
        */
    }


    private void openManuelLocationDialog() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            //TODO optional: show dialog for manuel zoom to city if no location permitted
            LatLng markerLatLng = DEFAULT_LOCATION;
            String markerTitle = "CityInput";

            map.addMarker(new MarkerOptions()
                    .title(markerTitle)
                    .position(markerLatLng));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, DEFAULT_ZOOM));
        };

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Select a location")
                //.setItems()
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates
        updateLocationUI(true);
    }
}