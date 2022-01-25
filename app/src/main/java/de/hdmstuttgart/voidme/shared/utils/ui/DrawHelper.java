package de.hdmstuttgart.voidme.shared.utils.ui;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.math.BigDecimal;

public class DrawHelper {
    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static int getColorInt(float hue) {
        return Color.HSVToColor(new float[]{
                hue, 1f, .6f
        });
    }

    /*public HSBType(Color color) {
        if (color != null) {
            float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            this.hue = BigDecimal.valueOf(hsbValues[0] * 360);
            this.saturation = BigDecimal.valueOf(hsbValues[1] * 100);
            this.value = BigDecimal.valueOf(hsbValues[2] * 100);
        } else {
            throw new IllegalArgumentException("Constructor argument must not be null");
        }
    }*/
}