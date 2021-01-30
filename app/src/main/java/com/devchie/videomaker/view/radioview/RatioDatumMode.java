package com.devchie.videomaker.view.radioview;

public enum RatioDatumMode {
    DATUM_AUTO(0),
    DATUM_WIDTH(1),
    DATUM_HEIGHT(2);
    
    final int mode;

    private RatioDatumMode(int i) {
        this.mode = i;
    }

    public static RatioDatumMode valueOf(int i) {
        RatioDatumMode ratioDatumMode = DATUM_WIDTH;
        if (i == ratioDatumMode.mode) {
            return ratioDatumMode;
        }
        RatioDatumMode ratioDatumMode2 = DATUM_HEIGHT;
        if (i == ratioDatumMode2.mode) {
            return ratioDatumMode2;
        }
        return DATUM_AUTO;
    }
}
