package com.devchie.videomaker.view.nativead;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

public class NativeTemplateStyle {

    public ColorDrawable callToActionBackgroundColor;

    public float callToActionTextSize;

    public Typeface callToActionTextTypeface;

    public int callToActionTypefaceColor;

    public ColorDrawable mainBackgroundColor;

    public ColorDrawable primaryTextBackgroundColor;

    public float primaryTextSize;

    public Typeface primaryTextTypeface;

    public int primaryTextTypefaceColor;

    public ColorDrawable secondaryTextBackgroundColor;

    public float secondaryTextSize;

    public Typeface secondaryTextTypeface;

    public int secondaryTextTypefaceColor;

    public ColorDrawable tertiaryTextBackgroundColor;

    public float tertiaryTextSize;

    public Typeface tertiaryTextTypeface;

    public int tertiaryTextTypefaceColor;

    public Typeface getCallToActionTextTypeface() {
        return this.callToActionTextTypeface;
    }

    public float getCallToActionTextSize() {
        return this.callToActionTextSize;
    }

    public int getCallToActionTypefaceColor() {
        return this.callToActionTypefaceColor;
    }

    public ColorDrawable getCallToActionBackgroundColor() {
        return this.callToActionBackgroundColor;
    }

    public Typeface getPrimaryTextTypeface() {
        return this.primaryTextTypeface;
    }

    public float getPrimaryTextSize() {
        return this.primaryTextSize;
    }

    public int getPrimaryTextTypefaceColor() {
        return this.primaryTextTypefaceColor;
    }

    public ColorDrawable getPrimaryTextBackgroundColor() {
        return this.primaryTextBackgroundColor;
    }

    public Typeface getSecondaryTextTypeface() {
        return this.secondaryTextTypeface;
    }

    public float getSecondaryTextSize() {
        return this.secondaryTextSize;
    }

    public int getSecondaryTextTypefaceColor() {
        return this.secondaryTextTypefaceColor;
    }

    public ColorDrawable getSecondaryTextBackgroundColor() {
        return this.secondaryTextBackgroundColor;
    }

    public Typeface getTertiaryTextTypeface() {
        return this.tertiaryTextTypeface;
    }

    public float getTertiaryTextSize() {
        return this.tertiaryTextSize;
    }

    public int getTertiaryTextTypefaceColor() {
        return this.tertiaryTextTypefaceColor;
    }

    public ColorDrawable getTertiaryTextBackgroundColor() {
        return this.tertiaryTextBackgroundColor;
    }

    public ColorDrawable getMainBackgroundColor() {
        return this.mainBackgroundColor;
    }

    public static class Builder {
        private NativeTemplateStyle styles = new NativeTemplateStyle();

        public Builder withCallToActionTextTypeface(Typeface typeface) {
           this.styles.callToActionTextTypeface = typeface;
            return this;
        }

        public Builder withCallToActionTextSize(float f) {
           this.styles.callToActionTextSize = f;
            return this;
        }

        public Builder withCallToActionTypefaceColor(int i) {
           this.styles.callToActionTypefaceColor = i;
            return this;
        }

        public Builder withCallToActionBackgroundColor(ColorDrawable colorDrawable) {
          this.styles.callToActionBackgroundColor = colorDrawable;
            return this;
        }

        public Builder withPrimaryTextTypeface(Typeface typeface) {
           this.styles.primaryTextTypeface = typeface;
            return this;
        }

        public Builder withPrimaryTextSize(float f) {
          this.styles.primaryTextSize = f;
            return this;
        }

        public Builder withPrimaryTextTypefaceColor(int i) {
          this.styles.primaryTextTypefaceColor = i;
            return this;
        }

        public Builder withPrimaryTextBackgroundColor(ColorDrawable colorDrawable) {
           this.styles.primaryTextBackgroundColor = colorDrawable;
            return this;
        }

        public Builder withSecondaryTextTypeface(Typeface typeface) {
            this.styles.secondaryTextTypeface = typeface;
            return this;
        }

        public Builder withSecondaryTextSize(float f) {
             this.styles.secondaryTextSize = f;
            return this;
        }

        public Builder withSecondaryTextTypefaceColor(int i) {
          this.styles.secondaryTextTypefaceColor = i;
            return this;
        }

        public Builder withSecondaryTextBackgroundColor(ColorDrawable colorDrawable) {
             this.styles.secondaryTextBackgroundColor = colorDrawable;
            return this;
        }

        public Builder withTertiaryTextTypeface(Typeface typeface) {
             this.styles.tertiaryTextTypeface = typeface;
            return this;
        }

        public Builder withTertiaryTextSize(float f) {
             this.styles.tertiaryTextSize = f;
            return this;
        }

        public Builder withTertiaryTextTypefaceColor(int i) {
          this.styles.tertiaryTextTypefaceColor = i;
            return this;
        }

        public Builder withTertiaryTextBackgroundColor(ColorDrawable colorDrawable) {
            this.styles.tertiaryTextBackgroundColor = colorDrawable;
            return this;
        }

        public Builder withMainBackgroundColor(ColorDrawable colorDrawable) {
            this.styles.mainBackgroundColor = colorDrawable;
            return this;
        }

        public NativeTemplateStyle build() {
            return this.styles;
        }
    }
}
