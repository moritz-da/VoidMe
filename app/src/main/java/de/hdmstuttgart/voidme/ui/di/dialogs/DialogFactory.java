package de.hdmstuttgart.voidme.ui.di.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;


public class DialogFactory {
    public static final int LOCATION_ENTRY_DIALOG = 1;

    public static Dialog create(int kind, View view, Context context, Activity activity) {
        switch (kind) {
            case LOCATION_ENTRY_DIALOG:
                return LocationEntryDialog.newInstance(view, context, activity);
            default: return null;
        }
    }
}

