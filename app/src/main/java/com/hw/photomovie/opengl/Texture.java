package com.hw.photomovie.opengl;


public interface Texture {
    public int getWidth();

    public int getHeight();

    public void draw(GLESCanvas canvas, int x, int y);

    public void draw(GLESCanvas canvas, int x, int y, int w, int h);

    public boolean isOpaque();
}
