package com.hw.photomovie.opengl;


public class FadeOutTexture extends FadeTexture {
    public FadeOutTexture(BasicTexture texture) {
        super(texture);
    }

    @Override
    public void draw(GLESCanvas canvas, int x, int y, int w, int h) {
        if (isAnimating()) {
            canvas.save(GLESCanvas.SAVE_FLAG_ALPHA);
            canvas.setAlpha(getRatio());
            mTexture.draw(canvas, x, y, w, h);
            canvas.restore();
        }
    }
}
