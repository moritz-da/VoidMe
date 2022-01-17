package de.hdmstuttgart.voidme.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class MapFragment extends Fragment {

    private static final String TAG = "-MAP-";
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Stuttgart GERMANY.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng stuttgartHdM = new LatLng(	48.741400, 9.100630);
            googleMap.addMarker(new MarkerOptions().position(stuttgartHdM).title("Hochschule der Medien, Stuttgart"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(stuttgart));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stuttgartHdM, 10));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
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
        View addBtn = view.findViewById(R.id.saveCurrentLocation);
        if (addBtn != null) {
            addBtn.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        this.requireActivity(),
                        R.style.SheetDialog
                );
                bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.new_entry_bottom_sheet, view.findViewById(R.id.addLocation_bottomSheet_container));

                Spinner category = bottomSheetView.findViewById(R.id.categorySelector);
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                        this.getContext(),
                        R.array.categories_array,
                        R.layout.spinner_color
                );

                arrayAdapter.setDropDownViewResource(R.layout.spinner_color_dropdown_layout);
                category.setAdapter(arrayAdapter);

                //Save and close on click
                View saveBtn = bottomSheetView.findViewById(R.id.saveNewLocation);
                if (saveBtn != null) {
                    saveBtn.setOnClickListener(v2 -> {
                        Log.i(TAG, "Saving new Entry...");
                        SeekBar severity = (SeekBar)bottomSheetDialog.findViewById(R.id.severityLevel);
                        //TODO
                        DbManager.voidLocation.locationDao().insert(new LocationEntity(
                                "Title",
                                "Description",
                                category.getSelectedItem().toString(),
                                "latitude",
                                "longitude",
                                "altitude",
                                "accuracy",
                                "address",
                                severity != null ? severity.getProgress() : 0
                        ));
                        Log.d(TAG, "Saving new Entry..." + DbManager.voidLocation.locationDao().getAll().toString());
                        Toast.makeText(getContext(), R.string.saved_new_location, Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    });
                }
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
        }
    }
}