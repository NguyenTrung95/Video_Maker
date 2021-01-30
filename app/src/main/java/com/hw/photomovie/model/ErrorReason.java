package com.hw.photomovie.model;



public class ErrorReason {

    private Throwable mThrowable;
    private String mExtra;

    public ErrorReason(Throwable cause,String extra) {
        mThrowable = cause;
        mExtra = extra;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public String getExtra() {
        return mExtra;
    }
}
