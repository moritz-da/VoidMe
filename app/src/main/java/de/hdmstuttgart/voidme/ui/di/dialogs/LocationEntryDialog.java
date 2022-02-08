package de.hdmstuttgart.voidme.ui.di.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class LocationEntryDialog extends Dialog {
    private static final String TAG = LocationEntryDialog.class.toString();
    /*private View addBtn;
    private View view;
    private Context context;
    private Activity activity;*/

    public LocationEntryDialog(@NonNull Context context) {
        super(context);
    }

    public static LocationEntryDialog newInstance(View view, Context context, Activity activity) {
        LocationEntryDialog locationEntryDialog = new LocationEntryDialog(context);
        locationEntryDialog.setOwnerActivity(activity);
        locationEntryDialog.setContentView(view);
        return locationEntryDialog;
    }


    private void locationEntryHandler() {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    this.getOwnerActivity(),
                    R.style.SheetDialog
            );
            bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            View bottomSheetView = LayoutInflater.from(this.getContext()).inflate(R.layout.new_entry_bottom_sheet, this.getWindow().findViewById(R.id.addLocation_bottomSheet_container));
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
                    /*Location location = new Location(LocationService.getInstance().getLocation(getActivity())); //TODO change origin later

                    DbManager.voidLocation.locationDao().insert(new LocationEntity(
                            title.getText().toString(),
                            description.getText().toString(),
                            category.getSelectedItem().toString(),
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getAltitude(),
                            location.getAccuracy(),
                            severity.getProgress()
                    ));*/
                    Log.d(TAG, "Saving new Entry..." + DbManager.voidLocation.locationDao().getAll().toString());
                    Toast.makeText(getContext(), R.string.location_saved, Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                });
            }
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
    }
}
