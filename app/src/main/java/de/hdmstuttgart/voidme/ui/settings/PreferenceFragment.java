package de.hdmstuttgart.voidme.ui.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import de.hdmstuttgart.voidme.MainActivity;
import de.hdmstuttgart.voidme.R;

/**
 *
 */
public abstract class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setting default theme background in the view so as to avoid transparency
        TypedValue typedValue = new TypedValue ();
        view.getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        view.setBackgroundColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & typedValue.data))));
    }
}
