package de.hdmstuttgart.voidme.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import de.hdmstuttgart.voidme.MainActivity;
import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.Location;
import de.hdmstuttgart.voidme.databinding.FragmentHomeBinding;
import de.hdmstuttgart.voidme.ui.settings.SettingsActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "-HOME-";
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
                        SeekBar severity = (SeekBar)bottomSheetDialog.findViewById(R.id.severityLevel);
                        //TODO
                        DbManager.voidLocation.locationDao().insert(new Location(
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}