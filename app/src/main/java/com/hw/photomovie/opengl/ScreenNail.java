package com.hw.photomovie.opengl;

import android.graphics.RectF;

public interface ScreenNail {
    public int getWidth();

    public int getHeight();

    public boolean isReady();

    public void setLoadingTexture(StringTexture loadingTexture);

    public void draw(GLESCanvas canvas, int x, int y, int width, int height);

    public void noDraw();

    public void recycle();

    public void draw(GLESCanvas canvas, RectF source, RectF dest);
}
