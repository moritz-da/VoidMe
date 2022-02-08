package de.hdmstuttgart.voidme.ui.di;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.shared.utils.ui.DrawHelper;

public class Helper {
    /**
     * Mapping for categories and their icons
     * @param category The category suiting a specific icon.
     * @param resources Class for accessing an application's resources.
     * @implNote should be enum in future
     * @return drawable icon
     */
    public static int getCategoryIcon(String category, Resources resources) {
        final String[] categories;
        categories = resources.getStringArray(R.array.categories_array);
        if (category.equals(categories[0])) return R.drawable.ic_round_groups_24;
        if (category.equals(categories[1])) return R.drawable.ic_round_flashlight_off_24;
        if (category.equals(categories[2])) return R.drawable.ic_round_car_crash_24;
        if (category.equals(categories[3])) return R.drawable.ic_round_stairs_24;
        if (category.equals(categories[4])) return R.drawable.ic_round_ac_unit_24;
        if (category.equals(categories[5])) return R.drawable.ic_round_masks_24;
        if (category.equals(categories[6])) return R.drawable.ic_round_liquor_24;
        if (category.equals(categories[7])) return R.drawable.ic_round_minor_crash_24;
        if (category.equals(categories[8])) return R.drawable.ic_round_back_hand_24;
        if (category.equals(categories[9])) return R.drawable.ic_round_money_off_24;
        if (category.equals(categories[10])) return R.drawable.ic_round_medication_liquid_24;
        if (category.equals(categories[11])) return R.drawable.ic_round_tsunami_24;
        if (category.equals(categories[12])) return R.drawable.ic_round_pest_control_24;
        if (category.equals(categories[13])) return R.drawable.ic_round_dangerous_24;
        return R.drawable.ic_round_bug_report_24;
    }

    /**
     * Bitmap descriptor factory for single drawable to bitmap descriptor
     * @param context Context of call to access application-specific resources.
     * @param vectorResId The resource ID of a drawable that needs to be converted.
     * @return Bitmap discriptor, default Maps Bitmap Descriptor if ID not valid
     */
    public static BitmapDescriptor bitmapDescriptorFromVector(@NonNull Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Bitmap bitmap;
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        return BitmapDescriptorFactory.defaultMarker();
    }

    /**
     * Bitmap descriptor factory for drawable to bitmap descriptor with back- and foreground
     * @param context Context of call to access application-specific resources.
     * @param foregroundVectorResId The resource ID of a drawable that will be set as foreground.
     * @param severity defines the color of the background, between [0,3]
     * @return Bitmap descriptor, default Maps Bitmap Descriptor if ID not valid
     */
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes  int foregroundVectorResId, int severity) {
        Bitmap bitmap;
        bitmap = vectorToBitmap(context, foregroundVectorResId, severity);
        if (bitmap != null) return BitmapDescriptorFactory.fromBitmap(bitmap);
        return BitmapDescriptorFactory.defaultMarker();
    }

    /**
     * Converts a drawable to a bitmap with colored marker as background
     * @param context Context of call to access application-specific resources.
     * @param foregroundVectorResId The resource ID of a drawable that will be set as foreground.
     * @param severity defines the color of the background, between [0,3]
     * @return Bitmap of the drawable and its background
     */
    public static Bitmap vectorToBitmap(Context context, @DrawableRes  int foregroundVectorResId, int severity) {
        Bitmap bitmap = null;
        Canvas canvas = null;
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_pin);
        if (background != null) {
            background.setColorFilter(DrawHelper.getColorInt(50f - (severity * 20f)), PorterDuff.Mode.SRC_ATOP);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            background.draw(canvas);
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, foregroundVectorResId);
        if (vectorDrawable != null) {
            vectorDrawable.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
            vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 20, vectorDrawable.getIntrinsicHeight() + 5);
            vectorDrawable.draw(canvas);
        }
        return bitmap;
    }
}