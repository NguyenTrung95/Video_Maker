package com.hw.photomovie.opengl;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

public interface GLESCanvas {

    public GLId getGLId();


    public abstract void setSize(int width, int height);

    public abstract void clearBuffer();

    public abstract void clearBuffer(float[] argb);

    public abstract void setAlpha(float alpha);

    public abstract float getAlpha();

    public abstract void multiplyAlpha(float alpha);

    public abstract void translate(float x, float y, float z);

    public abstract void translate(float x, float y);

    public abstract void scale(float sx, float sy, float sz);

    public abstract void rotate(float angle, float x, float y, float z);

    public abstract void multiplyMatrix(float[] matrix, int offset);

    public abstract void save();

    public abstract void save(int saveFlags);

    public static final int SAVE_FLAG_ALL = 0xFFFFFFFF;
    public static final int SAVE_FLAG_ALPHA = 0x01;
    public static final int SAVE_FLAG_MATRIX = 0x02;


    public abstract void restore();


    public abstract void drawLine(float x1, float y1, float x2, float y2, GLPaint paint);


    public abstract void drawRect(float x1, float y1, float x2, float y2, GLPaint paint);

    public abstract void fillRect(float x, float y, float width, float height, int color);

    public abstract void drawTexture(
            BasicTexture texture, int x, int y, int width, int height);

    public abstract void drawMesh(BasicTexture tex, int x, int y, int xyBuffer,
                                  int uvBuffer, int indexBuffer, int indexCount);

    public abstract void drawTexture(BasicTexture texture, RectF source, RectF target);

    public abstract void drawTexture(BasicTexture texture, float[] mTextureTransform,
                                     int x, int y, int w, int h);


    public abstract void drawMixed(BasicTexture from, int toColor,
                                   float ratio, int x, int y, int w, int h);


    public abstract void drawMixed(BasicTexture from, int toColor,
                                   float ratio, RectF src, RectF target);


    public abstract boolean unloadTexture(BasicTexture texture);

    public abstract void deleteBuffer(int bufferId);


    public abstract void deleteRecycledResources();

    // Dump statistics information and clear the counters. For debug only.
    public abstract void dumpStatisticsAndClear();

    public abstract void beginRenderTarget(RawTexture texture);

    public abstract void endRenderTarget();


    public abstract void setTextureParameters(BasicTexture texture);


    public abstract void initializeTextureSize(BasicTexture texture, int format, int type);


    public abstract void initializeTexture(BasicTexture texture, Bitmap bitmap);


    public abstract void texSubImage2D(BasicTexture texture, int xOffset, int yOffset,
                                       Bitmap bitmap,
                                       int format, int type);


    public abstract int uploadBuffer(java.nio.FloatBuffer buffer);


    public abstract int uploadBuffer(java.nio.ByteBuffer buffer);


    public abstract void recoverFromLightCycle();


    public abstract void getBounds(Rect bounds, int x, int y, int width, int height);


    public abstract void unbindArrayBuffer();

    public abstract void rebindArrayBuffer();

}
