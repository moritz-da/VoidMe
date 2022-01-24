package de.hdmstuttgart.voidme.ui.home;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.databinding.FragmentHomeBinding;
import de.hdmstuttgart.voidme.shared.utils.location.SaveEntryTask;
import de.hdmstuttgart.voidme.shared.exceptions.PermissionDeniedException;
import de.hdmstuttgart.voidme.ui.settings.SettingsActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "-HOME-";
    public static final int PERMISSIONS_FINE_LOCATION = 99;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageButton settingsBtn = view.findViewById(R.id.settingsBtn);

        settingsBtn.setOnClickListener(e -> {
            //NavUtils.navigateUpFromSameTask(this.requireActivity());
            Intent intent = new Intent(this.getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        //Add location dialog
        Button addBtn = view.findViewById(R.id.btn_add_location);
        if (addBtn != null) {
            addBtn.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        this.requireActivity(),
                        R.style.SheetDialog
                );
                bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.new_entry_bottom_sheet, view.findViewById(R.id.addLocation_bottomSheet_container));
                //bottomSheetView.setBackgroundColor(0);

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
                        SeekBar severity = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.severityLevel));
                        EditText title = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.locationName));
                        EditText description = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.locationDescription));

//                        LocationService locationService = LocationService.getInstance();
//                        Location location = locationService.getLocation(getActivity());
//                        // first system statement
//                        Log.d(TAG, "1. Lon: " + location.getLongitude());
//                        Log.d(TAG, "1. Lat: " + location.getLatitude());
//                        Log.d(TAG, "1. Alt: " + location.getAltitude());
//                        Log.d(TAG, "1. Acc: " + location.getAccuracy());
//                            LocationEntity locationEntity = new LocationEntity(
//                                    title.getText().toString(),
//                                    description.getText().toString(),
//                                    category.getSelectedItem().toString(),
//                                    location.getLatitude(),
//                                    location.getLongitude(),
//                                    location.getAltitude(),
//                                    location.getAccuracy(),
//                                    severity.getProgress()
//                            );
//                        DbManager.voidLocation.locationDao().insert(locationEntity);
//                        Toast.makeText(getContext(), R.string.saved_new_location, Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "New entry saved " + locationEntity);
//                        try {
                        SaveEntryTask saveEntryTask = new SaveEntryTask(getActivity());
                        saveEntryTask.execute(title.getText().toString(), description.getText().toString(),
                                category.getSelectedItem().toString(), Integer.toString(severity.getProgress()));
                        bottomSheetDialog.dismiss();
//                        } catch (SQLiteConstraintException ex) {
//                            Log.e(TAG, "Entry not saved!", ex);
//                            Toast.makeText(getContext(), R.string.location_exists, Toast.LENGTH_LONG).show();
//                        } catch (PermissionDeniedException permEx) {
//                            Log.e(TAG, "Entry not saved!", permEx);
//                            Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show();
//                            // TODO request permission
//                            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
//                        }
//                        Log.d(TAG, "DB: " + DbManager.voidLocation.locationDao().getAll());
                    });
                }
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case LocationService.PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission FineLoc", Toast.LENGTH_SHORT).show();

                }
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // TODO: Call MainActivity.setupPermissions();
                }
                else {
                    Toast.makeText(getActivity(), "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
        }
    }*/

}


/*
Geocoder geocoder = new Geocoder(this, Locale.getDefault());

addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
String city = addresses.get(0).getLocality();
String state = addresses.get(0).getAdminArea();
String country = addresses.get(0).getCountryName();
String postalCode = addresses.get(0).getPostalCode();
String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
* */