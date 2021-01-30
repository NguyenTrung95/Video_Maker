package com.devchie.videomaker.helper;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ColorFilterGenerator {
    private static double[] DELTA_INDEX = {0.0d, 0.01d, 0.02d, 0.04d, 0.05d, 0.06d, 0.07d, 0.08d, 0.1d, 0.11d, 0.12d, 0.14d, 0.15d, 0.16d, 0.17d, 0.18d, 0.2d, 0.21d, 0.22d, 0.24d, 0.25d, 0.27d, 0.28d, 0.3d, 0.32d, 0.34d, 0.36d, 0.38d, 0.4d, 0.42d, 0.44d, 0.46d, 0.48d, 0.5d, 0.53d, 0.56d, 0.59d, 0.62d, 0.65d, 0.68d, 0.71d, 0.74d, 0.77d, 0.8d, 0.83d, 0.86d, 0.89d, 0.92d, 0.95d, 0.98d, 1.0d, 1.06d, 1.12d, 1.18d, 1.24d, 1.3d, 1.36d, 1.42d, 1.48d, 1.54d, 1.6d, 1.66d, 1.72d, 1.78d, 1.84d, 1.9d, 1.96d, 2.0d, 2.12d, 2.25d, 2.37d, 2.5d, 2.62d, 2.75d, 2.87d, 3.0d, 3.2d, 3.4d, 3.6d, 3.8d, 4.0d, 4.3d, 4.7d, 4.9d, 5.0d, 5.5d, 6.0d, 6.5d, 6.8d, 7.0d, 7.3d, 7.5d, 7.8d, 8.0d, 8.4d, 8.7d, 9.0d, 9.4d, 9.6d, 9.8d, 10.0d};

    public static void adjustHue(ColorMatrix colorMatrix, float f) {
        float cleanValue = (cleanValue(f, 180.0f) / 180.0f) * 3.1415927f;
        if (cleanValue != 0.0f) {
            double d = (double) cleanValue;
            float cos = (float) Math.cos(d);
            float sin = (float) Math.sin(d);
            float f2 = (cos * -0.715f) + 0.715f;
            float f3 = (-0.072f * cos) + 0.072f;
            float f4 = (-0.213f * cos) + 0.213f;
            colorMatrix.postConcat(new ColorMatrix(new float[]{(0.787f * cos) + 0.213f + (sin * -0.213f), (-0.715f * sin) + f2, (sin * 0.928f) + f3, 0.0f, 0.0f, (0.143f * sin) + f4, (0.28500003f * cos) + 0.715f + (0.14f * sin), f3 + (-0.283f * sin), 0.0f, 0.0f, f4 + (-0.787f * sin), f2 + (0.715f * sin), (cos * 0.928f) + 0.072f + (sin * 0.072f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustBrightness(ColorMatrix colorMatrix, float f) {
        float cleanValue = cleanValue(f, 100.0f);
        if (cleanValue != 0.0f) {
            colorMatrix.postConcat(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, cleanValue, 0.0f, 1.0f, 0.0f, 0.0f, cleanValue, 0.0f, 0.0f, 1.0f, 0.0f, cleanValue, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustContrast(ColorMatrix colorMatrix, int i) {
        float f;
        int cleanValue = (int) cleanValue((float) i, 100.0f);
        if (cleanValue != 0) {
            if (cleanValue < 0) {
                f = ((float) cleanValue) / 100.0f;
            } else {
                float f2 = (float) (cleanValue % 1);
                if (f2 == 0.0f) {
                    f = (float) DELTA_INDEX[cleanValue];
                } else {
                    double[] dArr = DELTA_INDEX;
                    int i2 = cleanValue << 0;
                    f = (((float) dArr[i2 + 1]) * f2) + (((float) dArr[i2]) * (1.0f - f2));
                }
            }
            float f3 = (f * 127.0f) + 127.0f;
            float f4 = f3 / 127.0f;
            float f5 = (127.0f - f3) * 0.5f;
            colorMatrix.postConcat(new ColorMatrix(new float[]{f4, 0.0f, 0.0f, 0.0f, f5, 0.0f, f4, 0.0f, 0.0f, f5, 0.0f, 0.0f, f4, 0.0f, f5, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustSaturation(ColorMatrix colorMatrix, float f) {
        float cleanValue = cleanValue(f, 100.0f);
        int i = (cleanValue > 0.0f ? 1 : (cleanValue == 0.0f ? 0 : -1));
        if (i != 0) {
            if (i > 0) {
                cleanValue *= 3.0f;
            }
            float f2 = (cleanValue / 100.0f) + 1.0f;
            float f3 = 1.0f - f2;
            float f4 = 0.3086f * f3;
            float f5 = 0.6094f * f3;
            float f6 = f3 * 0.082f;
            colorMatrix.postConcat(new ColorMatrix(new float[]{f4 + f2, f5, f6, 0.0f, 0.0f, f4, f5 + f2, f6, 0.0f, 0.0f, f4, f5, f6 + f2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    protected static float cleanValue(float f, float f2) {
        return Math.min(f2, Math.max(-f2, f));
    }

    public static ColorFilter adjustColor(int i, int i2, int i3, int i4) {
        ColorMatrix colorMatrix = new ColorMatrix();
        adjustHue(colorMatrix, (float) i4);
        adjustContrast(colorMatrix, i2);
        adjustBrightness(colorMatrix, (float) i);
        adjustSaturation(colorMatrix, (float) i3);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    public static ColorFilter adjustColor(int i, int i2, int i3) {
        ColorMatrix colorMatrix = new ColorMatrix();
        adjustContrast(colorMatrix, i2);
        adjustBrightness(colorMatrix, (float) i);
        adjustSaturation(colorMatrix, (float) i3);
        return new ColorMatrixColorFilter(colorMatrix);
    }
}
