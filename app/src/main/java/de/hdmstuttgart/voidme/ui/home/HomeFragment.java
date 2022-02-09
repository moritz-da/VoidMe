package de.hdmstuttgart.voidme.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.databinding.FragmentHomeBinding;
import de.hdmstuttgart.voidme.shared.utils.location.SaveEntryTask;
import de.hdmstuttgart.voidme.ui.settings.SettingsActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "-HOME-";
    private FragmentHomeBinding binding;
    private boolean permissionsGranted = false;
    private BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView instructionHeader = binding.textHome;
        final TextView instruction = binding.textHomeBody;
        homeViewModel.getHeader().observe(getViewLifecycleOwner(), instructionHeader::setText);
        homeViewModel.getBody().observe(getViewLifecycleOwner(), instruction::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageButton settingsBtn = view.findViewById(R.id.settingsBtn);

        settingsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this.getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        //Add location dialog
        Button addBtn = view.findViewById(R.id.btn_add_location);
        if (addBtn != null) {
            addBtn.setOnClickListener(v -> {
                checkPermission();
                bottomSheetDialog = new BottomSheetDialog(
                        this.requireContext(),
                        R.style.SheetDialog
                );
                bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.new_entry_bottom_sheet,
                        view.findViewById(R.id.addLocation_bottomSheet_container));
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
                        SeekBar severity = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.severityLevel));
                        EditText title = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.locationName));
                        EditText description = Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.locationDescription));
                        saveEntry(title.getText().toString(), description.getText().toString(),
                                category.getSelectedItem().toString(), Integer.toString(severity.getProgress()));
                        bottomSheetDialog.dismiss();
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

    private void checkPermission() {
        // check permission
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = true;
        } else {
            permissionsGranted = false;
            Log.i(TAG, "...permission not granted...");
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(), R.string.permission_explanation, Toast.LENGTH_LONG).show();
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void saveEntry(String title, String description, String category, String severity) {
        Log.i(TAG, "Saving new Entry...");
        if (permissionsGranted) {
            Log.i(TAG, "...permission granted...");
            SaveEntryTask saveEntryTask = new SaveEntryTask(getActivity());
            saveEntryTask.execute(title, description, category, severity);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    permissionsGranted = true;
                } else {
                    Log.i(TAG, "...permission denied");
                    bottomSheetDialog.dismiss();
                    Toast.makeText(getContext(), R.string.permission_denied_dialog, Toast.LENGTH_LONG).show();
                }
            });
}