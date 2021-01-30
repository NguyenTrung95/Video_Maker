package com.hw.photomovie;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import com.hw.photomovie.model.ErrorReason;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.music.IMusicPlayer;
import com.hw.photomovie.music.MusicPlayer;
import com.hw.photomovie.render.GLMovieRenderer;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.MovieRenderer;
import com.hw.photomovie.segment.MovieSegment;
import com.hw.photomovie.timer.IMovieTimer;
import com.hw.photomovie.timer.MovieTimer;
import com.hw.photomovie.util.AppResources;
import com.hw.photomovie.util.MLog;
import java.io.FileDescriptor;
import java.util.List;

public class PhotoMoviePlayer implements IMovieTimer.MovieListener {
    protected static final float FIRST_SEGMENT_PREPARE_RATE = 0.05f;
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PREPARING = 1;
    private static final String TAG = "PhotoMoviePlayer";
    private int mCurrentState = 0;

    public boolean mLoop;
    private IMovieTimer.MovieListener mMovieListener;

    public MovieRenderer mMovieRenderer;
    private IMovieTimer mMovieTimer;

    public IMusicPlayer mMusicPlayer = new MusicPlayer();

    public OnPreparedListener mOnPreparedListener;

    public PhotoMovie mPhotoMovie;

    public interface OnPreparedListener {
        void onError(PhotoMoviePlayer photoMoviePlayer);

        void onPrepared(PhotoMoviePlayer photoMoviePlayer, int i, int i2);

        void onPreparing(PhotoMoviePlayer photoMoviePlayer, float f);
    }

    public PhotoMoviePlayer(Context context) {
        AppResources.getInstance().init(context.getResources());
    }

    public void setMusicPlayer(IMusicPlayer iMusicPlayer) {
        this.mMusicPlayer = iMusicPlayer;
    }

    public void setDataSource(PhotoMovie photoMovie) {
        PhotoMovie photoMovie2;
        MovieRenderer movieRenderer;
        PhotoMovie photoMovie3 = this.mPhotoMovie;
        if (!(photoMovie3 == null || (movieRenderer = this.mMovieRenderer) == null)) {
            movieRenderer.release(photoMovie3.getMovieSegments());
        }
        setStateValue(0);
        this.mPhotoMovie = photoMovie;
        MovieTimer movieTimer = new MovieTimer(this.mPhotoMovie);
        this.mMovieTimer = movieTimer;
        movieTimer.setMovieListener(this);
        MovieRenderer movieRenderer2 = this.mMovieRenderer;
        if (!(movieRenderer2 == null || (photoMovie2 = this.mPhotoMovie) == null)) {
            photoMovie2.setMovieRenderer(movieRenderer2);
            this.mMovieRenderer.setPhotoMovie(this.mPhotoMovie);
        }
        setLoop(this.mLoop);
    }

    public void setMovieRenderer(MovieRenderer movieRenderer) {
        PhotoMovie photoMovie;
        this.mMovieRenderer = movieRenderer;
        if (movieRenderer != null && (photoMovie = this.mPhotoMovie) != null) {
            photoMovie.setMovieRenderer(movieRenderer);
            this.mMovieRenderer.setPhotoMovie(this.mPhotoMovie);
        }
    }

    public void setMusic(String str) {
        this.mMusicPlayer.setDataSource(str);
    }

    public void setMusic(Context context, Uri uri, MusicPlayer.OnMusicOKListener onMusicOKListener) {
        this.mMusicPlayer.setDataSource(context, uri, onMusicOKListener);
    }

    public void setMusic(FileDescriptor fileDescriptor) {
        this.mMusicPlayer.setDataSource(fileDescriptor);
    }

    public void setMusic(AssetFileDescriptor assetFileDescriptor) {
        this.mMusicPlayer.setDataSource(assetFileDescriptor);
    }

    public IMusicPlayer getMusicPlayer() {
        return this.mMusicPlayer;
    }

    public void prepare() {
        PhotoMovie photoMovie = this.mPhotoMovie;
        if (photoMovie == null || photoMovie.getPhotoSource() == null) {
            throw new NullPointerException("PhotoSource is null!");
        }
        prepare(this.mPhotoMovie.getPhotoSource().size());
    }

    public void prepare(int i) {
        PhotoMovie photoMovie = this.mPhotoMovie;
        if (photoMovie == null || photoMovie.getPhotoSource() == null) {
            throw new NullPointerException("PhotoSource is null!");
        }
        setStateValue(1);
        this.mPhotoMovie.getPhotoSource().setOnSourcePreparedListener(new PhotoSource.OnSourcePrepareListener() {
            public void onError(PhotoSource photoSource, PhotoData photoData, ErrorReason errorReason) {
            }

            public void onPreparing(PhotoSource photoSource, float f) {
                if (PhotoMoviePlayer.this.mOnPreparedListener != null) {
                    PhotoMoviePlayer.this.mOnPreparedListener.onPreparing(PhotoMoviePlayer.this, f * 0.95f);
                }
            }

            public void onPrepared(PhotoSource photoSource, int i, List<PhotoData> list) {
                if (list == null || list.size() == 0) {
                    PhotoMoviePlayer.this.prepareFirstSegment(i, photoSource.size());
                } else if (photoSource.size() > 0) {
                    PhotoMoviePlayer.this.mPhotoMovie.reAllocPhoto();
                    PhotoMoviePlayer.this.prepareFirstSegment(i, photoSource.size() + list.size());
                } else {
                    if (PhotoMoviePlayer.this.mOnPreparedListener != null) {
                        PhotoMoviePlayer.this.mOnPreparedListener.onError(PhotoMoviePlayer.this);
                    }
                    PhotoMoviePlayer.this.setStateValue(-1);
                    MLog.e(PhotoMoviePlayer.TAG, "数据加载失败");
                }
            }
        });
        this.mPhotoMovie.getPhotoSource().prepare(i);
    }

    public void setStateValue(int i) {
        this.mCurrentState = i;
        MovieRenderer movieRenderer = this.mMovieRenderer;
        if (movieRenderer == null) {
            return;
        }
        if (i == -1 || i == 0) {
            this.mMovieRenderer.enableDraw(false);
        } else if (i == 1) {
            movieRenderer.enableDraw(false);
        } else if (i == 2) {
            movieRenderer.enableDraw(true);
        }
    }


    public void prepareFirstSegment(final int i, final int i2) {
        List movieSegments = this.mPhotoMovie.getMovieSegments();
        if (movieSegments == null || movieSegments.size() < 1) {
            setStateValue(2);
            if (this.mOnPreparedListener != null) {
                onPrepared(i, i2);
                return;
            }
            return;
        }
        final MovieSegment movieSegment = (MovieSegment) movieSegments.get(0);
        movieSegment.setOnSegmentPrepareListener(new MovieSegment.OnSegmentPrepareListener() {
            public void onSegmentPrepared(boolean z) {
                movieSegment.setOnSegmentPrepareListener((MovieSegment.OnSegmentPrepareListener) null);
                PhotoMoviePlayer.this.setStateValue(2);
                if (PhotoMoviePlayer.this.mOnPreparedListener != null) {
                    PhotoMoviePlayer.this.mOnPreparedListener.onPreparing(PhotoMoviePlayer.this, 1.0f);
                    PhotoMoviePlayer.this.onPrepared(i, i2);
                }
            }
        });
        movieSegment.prepare();
    }

    public void seekTo(int i) {
        onMovieUpdate(i);
    }

    public int getCurrentPlayTime() {
        return this.mMovieTimer.getCurrentPlayTime();
    }

    public void pause() {
        IMovieTimer iMovieTimer = this.mMovieTimer;
        if (iMovieTimer != null) {
            iMovieTimer.pause();
        }
    }

    public void start() {
        if (!isPrepared()) {
            MLog.e(TAG, "start error!not prepared!");
            return;
        }
        if (this.mCurrentState != 4) {
            this.mPhotoMovie.calcuDuration();
        }
        IMovieTimer iMovieTimer = this.mMovieTimer;
        if (iMovieTimer != null) {
            iMovieTimer.start();
        }
    }

    public void stop() {
        if (this.mCurrentState >= 2) {
            pause();
            seekTo(0);
        }
    }

    public boolean isPlaying() {
        return this.mCurrentState == 3;
    }



    private boolean isInPlaybackState() {
        return (mPhotoMovie != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void setMovieListener(IMovieTimer.MovieListener movieListener) {
        this.mMovieListener = movieListener;
    }

    public void onMovieUpdate(int i) {
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieUpdate(i);
        }
        PhotoMovie photoMovie = this.mPhotoMovie;
        if (photoMovie != null) {
            photoMovie.updateProgress(i);
        }
    }

    public void onMovieStarted() {
        MLog.i(TAG, "onMovieStarted");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieStarted();
        }
        this.mMusicPlayer.start();
        setStateValue(3);
    }

    public void onMoviePaused() {
        MLog.i(TAG, "onMoviePaused");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMoviePaused();
        }
        this.mMusicPlayer.pause();
        setStateValue(4);
    }

    public void onMovieResumed() {
        MLog.i(TAG, "onMovieResumed");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieResumed();
        }
        this.mMusicPlayer.start();
        setStateValue(3);
    }

    public void onMovieEnd() {
        MLog.i(TAG, "onMovieEnd");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieEnd();
        }
        new StopMusicTask().execute(new Void[0]);
    }

    class StopMusicTask extends AsyncTask<Void, Void, Void> {
        StopMusicTask() {
        }


        public Void doInBackground(Void... voidArr) {
            PhotoMoviePlayer.this.mMusicPlayer.stop();
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            PhotoMoviePlayer.this.setStateValue(5);
            if (PhotoMoviePlayer.this.mLoop) {
                PhotoMoviePlayer.this.releaseAndRestart();
            } else {
                PhotoMoviePlayer.this.mMovieRenderer.release();
            }
        }
    }


    public void releaseAndRestart() {
        MovieRenderer movieRenderer = this.mMovieRenderer;
        if (!(movieRenderer instanceof GLSurfaceMovieRenderer) || ((GLSurfaceMovieRenderer) movieRenderer).isSurfaceCreated()) {
            final Handler handler = new Handler();
            this.mMovieRenderer.setOnReleaseListener(new MovieRenderer.OnReleaseListener() {
                public void onRelease() {
                    PhotoMoviePlayer.this.mMovieRenderer.setOnReleaseListener((MovieRenderer.OnReleaseListener) null);
                    handler.post(new Runnable() {
                        public void run() {
                            PhotoMoviePlayer.this.restartImpl();
                        }
                    });
                }
            });
            this.mMovieRenderer.release();
            return;
        }
        restartImpl();
    }


    public void restartImpl() {
        List movieSegments = this.mPhotoMovie.getMovieSegments();
        if (movieSegments != null && movieSegments.size() != 0) {
            setStateValue(1);
            final MovieSegment movieSegment = (MovieSegment) movieSegments.get(0);
            movieSegment.setOnSegmentPrepareListener(new MovieSegment.OnSegmentPrepareListener() {
                public void onSegmentPrepared(boolean z) {
                    movieSegment.setOnSegmentPrepareListener((MovieSegment.OnSegmentPrepareListener) null);
                    PhotoMoviePlayer.this.setStateValue(2);
                    PhotoMoviePlayer.this.start();
                }
            });
            movieSegment.prepare();
        }
    }

    public void destroy() {
        pause();
        setMovieListener((IMovieTimer.MovieListener) null);
        setOnPreparedListener((OnPreparedListener) null);
        IMovieTimer iMovieTimer = this.mMovieTimer;
        if (iMovieTimer != null) {
            iMovieTimer.setMovieListener((IMovieTimer.MovieListener) null);
        }
        this.mMovieTimer = null;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }


    public void onPrepared(final int i, final int i2) {
        MovieRenderer movieRenderer = this.mMovieRenderer;
        if (movieRenderer instanceof GLMovieRenderer) {
            ((GLMovieRenderer) movieRenderer).checkGLPrepared(new GLMovieRenderer.OnGLPrepareListener() {
                public void onGLPrepared() {
                    PhotoMoviePlayer.this.mOnPreparedListener.onPrepared(PhotoMoviePlayer.this, i, i2);
                }
            });
        } else {
            this.mOnPreparedListener.onPrepared(this, i, i2);
        }
    }

    public int getState() {
        return this.mCurrentState;
    }

    public void setLoop(boolean z) {
        this.mLoop = z;
    }

    public boolean isPrepared() {
        int i = this.mCurrentState;
        return i == 2 || i == 4 || i == 5;
    }
}
