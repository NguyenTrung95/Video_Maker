package com.hw.photomovie.timer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.timer.IMovieTimer;

public class MovieTimer implements IMovieTimer, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private final ValueAnimator mAnimator;
    private boolean mLoop;
    private IMovieTimer.MovieListener mMovieListener;
    private boolean mPaused;
    private long mPausedPlayTime;
    private PhotoMovie mPhotoMovie;

    public void onAnimationRepeat(Animator animator) {
    }

    public MovieTimer(PhotoMovie photoMovie) {
        this.mPhotoMovie = photoMovie;
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, 1});
        this.mAnimator = ofInt;
        ofInt.setInterpolator(new LinearInterpolator());
        this.mAnimator.addUpdateListener(this);
        this.mAnimator.addListener(this);
        this.mAnimator.setDuration(Long.MAX_VALUE);
    }

    public void start() {
        if (!this.mPaused) {
            this.mAnimator.start();
        } else {
            this.mAnimator.start();
        }
    }

    public void pause() {
        if (!this.mPaused) {
            this.mPaused = true;
            this.mPausedPlayTime = this.mAnimator.getCurrentPlayTime();
            this.mAnimator.cancel();
        }
    }

    public void setMovieListener(IMovieTimer.MovieListener movieListener) {
        this.mMovieListener = movieListener;
    }

    public int getCurrentPlayTime() {
        return (int) this.mPausedPlayTime;
    }

    public void setLoop(boolean z) {
        this.mLoop = z;
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (!this.mPaused && valueAnimator.isRunning()) {
            long currentPlayTime = valueAnimator.getCurrentPlayTime();
            if (currentPlayTime >= ((long) this.mPhotoMovie.getDuration())) {
                this.mAnimator.removeUpdateListener(this);
                this.mAnimator.removeListener(this);
                this.mAnimator.end();
                IMovieTimer.MovieListener movieListener = this.mMovieListener;
                if (movieListener != null) {
                    movieListener.onMovieEnd();
                }
                this.mAnimator.addUpdateListener(this);
                this.mAnimator.addListener(this);
                if (this.mLoop) {
                    this.mAnimator.start();
                    return;
                }
                return;
            }
            IMovieTimer.MovieListener movieListener2 = this.mMovieListener;
            if (movieListener2 != null) {
                movieListener2.onMovieUpdate((int) currentPlayTime);
            }
        }
    }

    public void onAnimationStart(Animator animator) {
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            if (this.mPaused) {
                movieListener.onMovieResumed();
            } else {
                movieListener.onMovieStarted();
            }
        }
        if (this.mPaused) {
            this.mAnimator.setCurrentPlayTime(this.mPausedPlayTime);
        }
        this.mPaused = false;
        this.mPausedPlayTime = 0;
    }

    public void onAnimationEnd(Animator animator) {
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener == null) {
            return;
        }
        if (this.mPaused) {
            movieListener.onMoviePaused();
        } else {
            movieListener.onMovieEnd();
        }
    }

    public void onAnimationCancel(Animator animator) {
        this.mPausedPlayTime = this.mAnimator.getCurrentPlayTime();
    }
}
