package com.devchie.videomaker.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.devchie.videomaker.R;

public class PlayPauseView extends View {
    private boolean isPlaying;
    private int mAnimDuration = 200;
    private int mBgColor = -1;
    private int mBtnColor = -16777216;
    private int mDirection = Direction.POSITIVE.value;
    private float mGapWidth;
    private int mHeight;
    private Path mLeftPath;
    private float mPadding;
    private Paint mPaint;

    public PlayPauseListener mPlayPauseListener;

    public float mProgress;
    private float mRadius;
    private Rect mRect;
    private float mRectHeight;
    private float mRectLT;
    private float mRectWidth;
    private Path mRightPath;
    private int mWidth;

    public interface PlayPauseListener {
        void pause();

        void play();
    }

    public PlayPauseView(Context context) {
        super(context);
    }

    public PlayPauseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public PlayPauseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        int[] PlayPauseView = {R.attr.anim_direction, R.attr.anim_duration, R.attr.bg_color, R.attr.btn_color, R.attr.gap_width, R.attr.space_padding};
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mLeftPath = new Path();
        this.mRightPath = new Path();
        this.mRect = new Rect();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, PlayPauseView);
        this.mBgColor = obtainStyledAttributes.getColor(2, -1);
        this.mBtnColor = obtainStyledAttributes.getColor(3, -16777216);
        this.mGapWidth = (float) obtainStyledAttributes.getDimensionPixelSize(4, dp2px(context, 0.0f));
        this.mPadding = (float) obtainStyledAttributes.getDimensionPixelSize(5, dp2px(context, 0.0f));
        this.mDirection = obtainStyledAttributes.getInt(0, Direction.POSITIVE.value);
        this.mAnimDuration = obtainStyledAttributes.getInt(1, 200);
        obtainStyledAttributes.recycle();
        setLayerType(1, (Paint) null);
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = View.MeasureSpec.getSize(i);
        this.mHeight = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        if (mode == View.MeasureSpec.EXACTLY) {
            this.mWidth = Math.min(this.mWidth, this.mHeight);
        } else {
            this.mWidth = dp2px(getContext(), 50.0f);
        }
        if (mode2 == View.MeasureSpec.EXACTLY) {
            this.mHeight = Math.min(this.mWidth, this.mHeight);
        } else {
            this.mHeight = dp2px(getContext(), 50.0f);
        }
        int min = Math.min(this.mWidth, this.mHeight);
        this.mHeight = min;
        this.mWidth = min;
        setMeasuredDimension(min, min);
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mHeight = i;
        this.mWidth = i;
        initValue();
    }

    private void initValue() {
        this.mRadius = (float) (this.mWidth / 2);
        float f = 0.0f;
        this.mPadding = getSpacePadding() == 0.0f ? this.mRadius / 3.0f : getSpacePadding();
        if (((double) getSpacePadding()) > ((double) this.mRadius) / Math.sqrt(2.0d) || this.mPadding < 0.0f) {
            this.mPadding = this.mRadius / 3.0f;
        }
        float sqrt = (float) ((((double) this.mRadius) / Math.sqrt(2.0d)) - ((double) this.mPadding));
        float f2 = this.mRadius;
        float f3 = f2 - sqrt;
        this.mRectLT = f3;
        this.mRect.top = (int) f3;
        int i = (int) (f2 + sqrt);
        this.mRect.bottom = i;
        this.mRect.left = (int) this.mRectLT;
        this.mRect.right = i;
        float f4 = sqrt * 2.0f;
        this.mRectWidth = f4;
        this.mRectHeight = f4;
        this.mGapWidth = getGapWidth() != 0.0f ? getGapWidth() : this.mRectWidth / 3.0f;
        if (!this.isPlaying) {
            f = 1.0f;
        }
        this.mProgress = f;
        this.mAnimDuration = getAnimDuration() < 0 ? 200 : getAnimDuration();
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mLeftPath.rewind();
        this.mRightPath.rewind();
        this.mPaint.setColor(this.mBgColor);
        canvas.drawCircle((float) (this.mWidth / 2), (float) (this.mHeight / 2), this.mRadius, this.mPaint);
        float f = this.mGapWidth;
        float f2 = this.mProgress;
        float f3 = f * (1.0f - f2);
        float f4 = (this.mRectWidth / 2.0f) - (f3 / 2.0f);
        float f5 = f4 * f2;
        float f6 = f4 + f3;
        float f7 = (f4 * 2.0f) + f3;
        float f8 = f7 - (f2 * f4);
        this.mPaint.setColor(this.mBtnColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        if (this.mDirection == Direction.NEGATIVE.value) {
            Path path = this.mLeftPath;
            float f9 = this.mRectLT;
            path.moveTo(f9, f9);
            Path path2 = this.mLeftPath;
            float f10 = this.mRectLT;
            path2.lineTo(f5 + f10, this.mRectHeight + f10);
            Path path3 = this.mLeftPath;
            float f11 = this.mRectLT;
            path3.lineTo(f4 + f11, this.mRectHeight + f11);
            Path path4 = this.mLeftPath;
            float f12 = this.mRectLT;
            path4.lineTo(f4 + f12, f12);
            this.mLeftPath.close();
            Path path5 = this.mRightPath;
            float f13 = this.mRectLT;
            path5.moveTo(f6 + f13, f13);
            Path path6 = this.mRightPath;
            float f14 = this.mRectLT;
            path6.lineTo(f6 + f14, this.mRectHeight + f14);
            Path path7 = this.mRightPath;
            float f15 = this.mRectLT;
            path7.lineTo(f8 + f15, this.mRectHeight + f15);
            Path path8 = this.mRightPath;
            float f16 = this.mRectLT;
            path8.lineTo(f7 + f16, f16);
            this.mRightPath.close();
        } else {
            Path path9 = this.mLeftPath;
            float f17 = this.mRectLT;
            path9.moveTo(f5 + f17, f17);
            Path path10 = this.mLeftPath;
            float f18 = this.mRectLT;
            path10.lineTo(f18, this.mRectHeight + f18);
            Path path11 = this.mLeftPath;
            float f19 = this.mRectLT;
            path11.lineTo(f4 + f19, this.mRectHeight + f19);
            Path path12 = this.mLeftPath;
            float f20 = this.mRectLT;
            path12.lineTo(f4 + f20, f20);
            this.mLeftPath.close();
            Path path13 = this.mRightPath;
            float f21 = this.mRectLT;
            path13.moveTo(f6 + f21, f21);
            Path path14 = this.mRightPath;
            float f22 = this.mRectLT;
            path14.lineTo(f6 + f22, this.mRectHeight + f22);
            Path path15 = this.mRightPath;
            float f23 = this.mRectLT;
            path15.lineTo(f6 + f23 + f4, this.mRectHeight + f23);
            Path path16 = this.mRightPath;
            float f24 = this.mRectLT;
            path16.lineTo(f8 + f24, f24);
            this.mRightPath.close();
        }
        canvas.save();
        canvas.translate((this.mRectHeight / 8.0f) * this.mProgress, 0.0f);
        float f25 = this.isPlaying ? 1.0f - this.mProgress : this.mProgress;
        float f26 = (float) (this.mDirection == Direction.NEGATIVE.value ? -90 : 90);
        if (this.isPlaying) {
            f25 += 1.0f;
        }
        canvas.rotate(f26 * f25, ((float) this.mWidth) / 2.0f, ((float) this.mHeight) / 2.0f);
        canvas.drawPath(this.mLeftPath, this.mPaint);
        canvas.drawPath(this.mRightPath, this.mPaint);
        canvas.restore();
    }

    public ValueAnimator getPlayPauseAnim() {
        float[] fArr = new float[2];
        float f = 1.0f;
        fArr[0] = this.isPlaying ? 1.0f : 0.0f;
        if (this.isPlaying) {
            f = 0.0f;
        }
        fArr[1] = f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setDuration((long) this.mAnimDuration);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PlayPauseView.this.mProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PlayPauseView.this.invalidate();
            }
        });
        return ofFloat;
    }

    public void play() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(true);
        getPlayPauseAnim().start();
    }

    public void pause() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(false);
        getPlayPauseAnim().start();
    }

    public void setPlayPauseListener(PlayPauseListener playPauseListener) {
        this.mPlayPauseListener = playPauseListener;
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (PlayPauseView.this.isPlaying()) {
                    PlayPauseView.this.pause();
                    if (PlayPauseView.this.mPlayPauseListener != null) {
                        PlayPauseView.this.mPlayPauseListener.pause();
                        return;
                    }
                    return;
                }
                PlayPauseView.this.play();
                if (PlayPauseView.this.mPlayPauseListener != null) {
                    PlayPauseView.this.mPlayPauseListener.play();
                }
            }
        });
    }

    public int dp2px(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean z) {
        this.isPlaying = z;
    }

    public void setGapWidth(float f) {
        this.mGapWidth = f;
    }

    public float getGapWidth() {
        return this.mGapWidth;
    }

    public int getBgColor() {
        return this.mBgColor;
    }

    public int getBtnColor() {
        return this.mBtnColor;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public void setBgColor(int i) {
        this.mBgColor = i;
    }

    public void setBtnColor(int i) {
        this.mBtnColor = i;
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction.value;
    }

    public float getSpacePadding() {
        return this.mPadding;
    }

    public void setSpacePadding(float f) {
        this.mPadding = f;
    }

    public int getAnimDuration() {
        return this.mAnimDuration;
    }

    public void setAnimDuration(int i) {
        this.mAnimDuration = i;
    }

    public enum Direction {
        POSITIVE(1),
        NEGATIVE(2);
        
        int value;

        private Direction(int i) {
            this.value = i;
        }
    }
}
