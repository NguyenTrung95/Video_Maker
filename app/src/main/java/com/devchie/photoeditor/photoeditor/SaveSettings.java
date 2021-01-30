package com.devchie.photoeditor.photoeditor;

public class SaveSettings {
    private boolean isClearViewsEnabled;
    private boolean isTransparencyEnabled;


    public boolean isTransparencyEnabled() {
        return this.isTransparencyEnabled;
    }


    public boolean isClearViewsEnabled() {
        return this.isClearViewsEnabled;
    }

    private SaveSettings(Builder builder) {
        this.isClearViewsEnabled = builder.isClearViewsEnabled;
        this.isTransparencyEnabled = builder.isTransparencyEnabled;
    }

    public static class Builder {

        public boolean isClearViewsEnabled = true;

        public boolean isTransparencyEnabled = true;

        public Builder setTransparencyEnabled(boolean z) {
            this.isTransparencyEnabled = z;
            return this;
        }

        public Builder setClearViewsEnabled(boolean z) {
            this.isClearViewsEnabled = z;
            return this;
        }

        public SaveSettings build() {
            return new SaveSettings(this);
        }
    }
}
