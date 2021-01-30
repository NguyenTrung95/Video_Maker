package com.hw.photomovie.opengl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;


abstract class CanvasTexture extends UploadedTexture {
    protected Canvas mCanvas;
    private final Config mConfig;

    public CanvasTexture(int width, int height) {
        mConfig = Config.ARGB_8888;
        setSize(width, height);
        setOpaque(false);
    }

    @Override
    protected Bitmap onGetBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, mConfig);
        mCanvas = new Canvas(bitmap);
        onDraw(mCanvas, bitmap);
        return bitmap;
    }

    @Override
    protected void onFreeBitmap(Bitmap bitmap) {
        if (!inFinalizer()) {
            bitmap.recycle();
        }
    }

    protected abstract void onDraw(Canvas canvas, Bitmap backing);
}
