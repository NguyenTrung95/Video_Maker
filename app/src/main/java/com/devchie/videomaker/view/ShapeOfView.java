package com.devchie.videomaker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import com.devchie.videomaker.R;

public class ShapeOfView extends FrameLayout {
    private Bitmap clipBitmap;

    public ClipManager clipManager = new ClipPathManager();
    private final Paint clipPaint = new Paint(1);
    private final Path clipPath = new Path();
    protected Drawable drawable = null;
    protected PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    final Path rectView = new Path();
    private boolean requiersShapeUpdate = true;

    public void setBackground(Drawable drawable2) {
    }

    public void setBackgroundColor(int i) {
    }

    public void setBackgroundResource(int i) {
    }

    public ShapeOfView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public ShapeOfView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ShapeOfView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        int resourceId;
        this.clipPaint.setAntiAlias(true);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
        this.clipPaint.setColor(-16776961);
        this.clipPaint.setStyle(Paint.Style.FILL);
        this.clipPaint.setStrokeWidth(1.0f);
        if (Build.VERSION.SDK_INT <= 27) {
            this.clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            setLayerType(View.LAYER_TYPE_SOFTWARE, this.clipPaint);
        } else {
            this.clipPaint.setXfermode(this.pdMode);
            setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) null);
        }
        if (attributeSet != null) {
            int[] ShapeOfView = {R.attr.shape_clip_drawable};
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet,ShapeOfView);
            if (obtainStyledAttributes.hasValue(0) && -1 != (resourceId = obtainStyledAttributes.getResourceId(0, -1))) {
                setDrawable(resourceId);
            }
            obtainStyledAttributes.recycle();
        }
    }


    public float dpToPx(float f) {
        return f * getContext().getResources().getDisplayMetrics().density;
    }


    public float pxToDp(float f) {
        return f / getContext().getResources().getDisplayMetrics().density;
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            requiresShapeUpdate();
        }
    }


    private boolean requiresBitmap() {
        ClipManager clipManager2 = null;
        return isInEditMode() || (clipManager2 != null && clipManager2.requiresBitmap()) || this.drawable != null;
    }

    public void setDrawable(Drawable drawable2) {
        this.drawable = drawable2;
        requiresShapeUpdate();
    }

    public void setDrawable(int i) {
        setDrawable(AppCompatResources.getDrawable(getContext(), i));
    }


    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.requiersShapeUpdate) {
            calculateLayout(canvas.getWidth(), canvas.getHeight());
            this.requiersShapeUpdate = false;
        }
        if (requiresBitmap()) {
            this.clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(this.clipBitmap, 0.0f, 0.0f, this.clipPaint);
        } else if (Build.VERSION.SDK_INT <= 27) {
            canvas.drawPath(this.clipPath, this.clipPaint);
        } else {
            canvas.drawPath(this.rectView, this.clipPaint);
        }
        if (Build.VERSION.SDK_INT <= 27) {
            setLayerType(View.LAYER_TYPE_HARDWARE, (Paint) null);
        }
    }

    private void calculateLayout(int i, int i2) {
        this.rectView.reset();
        this.rectView.addRect(0.0f, 0.0f, ((float) getWidth()) * 1.0f, ((float) getHeight()) * 1.0f, Path.Direction.CW);
        ClipManager clipManager2 = this.clipManager;
        if (clipManager2 != null && i > 0 && i2 > 0) {
            clipManager2.setupClipLayout(i, i2);
            this.clipPath.reset();
            this.clipPath.set(this.clipManager.createMask(i, i2));
            if (requiresBitmap()) {
                Bitmap bitmap = this.clipBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                this.clipBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.clipBitmap);
                Drawable drawable2 = this.drawable;
                if (drawable2 != null) {
                    drawable2.setBounds(0, 0, i, i2);
                    this.drawable.draw(canvas);
                } else {
                    canvas.drawPath(this.clipPath, this.clipManager.getPaint());
                }
            }
            if (Build.VERSION.SDK_INT > 27) {
                this.rectView.op(this.clipPath, Path.Op.DIFFERENCE);
            }
            if (Build.VERSION.SDK_INT >= 21 && ViewCompat.getElevation(this) > 0.0f) {
                try {
                    setOutlineProvider(getOutlineProvider());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        postInvalidate();
    }

    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                Path shadowConvexPath;
                if (ShapeOfView.this.clipManager != null && !ShapeOfView.this.isInEditMode() && (shadowConvexPath = ShapeOfView.this.clipManager.getShadowConvexPath()) != null) {
                    try {
                        outline.setConvexPath(shadowConvexPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void setClipPathCreator(ClipPathManager.ClipPathCreator clipPathCreator) {
        ((ClipPathManager) this.clipManager).setClipPathCreator(clipPathCreator);
        requiresShapeUpdate();
    }

    public void requiresShapeUpdate() {
        this.requiersShapeUpdate = true;
        postInvalidate();
    }
}
