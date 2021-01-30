package com.devchie.photoeditor.photoeditor;

import android.graphics.PointF;

class Vector2D extends PointF {
    public Vector2D() {
    }

    public Vector2D(float f, float f2) {
        super(f, f2);
    }

    public static float getAngle(Vector2D vector2D, Vector2D vector2D2) {
        vector2D.normalize();
        vector2D2.normalize();
        return (float) ((Math.atan2((double) vector2D2.y, (double) vector2D2.x) - Math.atan2((double) vector2D.y, (double) vector2D.x)) * 57.29577951308232d);
    }

    public void normalize() {
        float sqrt = (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y)));
        this.x /= sqrt;
        this.y /= sqrt;
    }
}
