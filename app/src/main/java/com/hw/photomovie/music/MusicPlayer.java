package com.hw.photomovie.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import java.io.FileDescriptor;
import java.io.IOException;

public class MusicPlayer implements IMusicPlayer {
    private static final int FADE_DURATION = 1800;
    private FadeOutRunnable mFadeOutRunnable;

    public MediaPlayer mMediaPlayer = new MediaPlayer();
    private MediaPlayer.OnErrorListener mOnErrorListener;

    public OnMusicOKListener onMusicOKListener;

    public interface OnMusicOKListener {
        void onMusicOK();
    }

    public void start() {
        FadeOutRunnable fadeOutRunnable = this.mFadeOutRunnable;
        if (fadeOutRunnable != null) {
            fadeOutRunnable.cancel();
            this.mFadeOutRunnable = null;
        }
        if (!isPlaying()) {
            safeSetVolume(1.0f);
            try {
                this.mMediaPlayer.setLooping(true);
                this.mMediaPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (isPlaying()) {
            this.mMediaPlayer.stop();
            try {
                this.mMediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mMediaPlayer.seekTo(0);
        }
    }


    public void safeSetVolume(float f) {
        try {
            this.mMediaPlayer.setVolume(f, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fadeStop(Handler handler) {
        FadeOutRunnable fadeOutRunnable = new FadeOutRunnable(handler, FADE_DURATION);
        this.mFadeOutRunnable = fadeOutRunnable;
        handler.postDelayed(fadeOutRunnable, 1000);
    }

    public void pause() {
        if (isPlaying()) {
            this.mMediaPlayer.pause();
        }
    }

    public void release() {
        this.mMediaPlayer.release();
    }

    public void setDataSource(String str) {
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(str);
            this.mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            MediaPlayer.OnErrorListener onErrorListener = this.mOnErrorListener;
            if (onErrorListener != null) {
                onErrorListener.onError(this.mMediaPlayer, -1004, 0);
            }
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
           MediaPlayer.OnErrorListener onErrorListener2 = this.mOnErrorListener;
            if (onErrorListener2 != null) {
                onErrorListener2.onError(this.mMediaPlayer, -1004, 0);
            }
        }
    }

    public void setDataSource(FileDescriptor fileDescriptor) {
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(fileDescriptor);
            this.mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            MediaPlayer.OnErrorListener onErrorListener = this.mOnErrorListener;
            if (onErrorListener != null) {
                onErrorListener.onError(this.mMediaPlayer, -1004, 0);
            }
        }
    }

    public void setDataSource(AssetFileDescriptor assetFileDescriptor) {
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            this.mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            MediaPlayer.OnErrorListener onErrorListener = this.mOnErrorListener;
            if (onErrorListener != null) {
                onErrorListener.onError(this.mMediaPlayer, -1004, 0);
            }
        }
    }



    public void setDataSource(Context context, Uri uri, OnMusicOKListener onMusicOKListener2) {
        this.onMusicOKListener = onMusicOKListener2;
        new SetDataSourceTask(context, uri).execute(new Void[0]);
    }

    class SetDataSourceTask extends AsyncTask<Void, Void, Void> {
        Context ctx;
        Uri uri;

        public SetDataSourceTask(Context context, Uri uri2) {
            this.ctx = context;
            this.uri = uri2;
        }


        public void onPreExecute() {
            super.onPreExecute();
            MusicPlayer.this.mMediaPlayer.reset();
        }


        public Void doInBackground(Void... voidArr) {
            try {
                MusicPlayer.this.mMediaPlayer.setDataSource(this.ctx, this.uri);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            MusicPlayer.this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (MusicPlayer.this.onMusicOKListener != null) {
                        MusicPlayer.this.onMusicOKListener.onMusicOK();
                    }
                }
            });
            try {
                MusicPlayer.this.mMediaPlayer.prepareAsync();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void setErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
        this.mMediaPlayer.setOnErrorListener(onErrorListener);
    }

    public void setLooping(boolean z) {
        this.mMediaPlayer.setLooping(z);
    }

    public boolean isPlaying() {
        try {
            return this.mMediaPlayer.isPlaying();
        } catch (IllegalStateException unused) {
            return false;
        }
    }

    public void seekTo(int i) {
        this.mMediaPlayer.seekTo(i);
    }

    class FadeOutRunnable implements Runnable {
        private boolean mCancel;
        private int mDuration;
        private Handler mHandler;
        private long mStartTime;

        public FadeOutRunnable(Handler handler, int i) {
            this.mDuration = i;
            this.mHandler = handler;
        }

        public void run() {
            if (!this.mCancel) {
                long currentTimeMillis = System.currentTimeMillis();
                if (this.mStartTime == 0) {
                    this.mStartTime = currentTimeMillis;
                }
                long j = this.mStartTime;
                int i = this.mDuration;
                if (currentTimeMillis - j > ((long) i)) {
                    synchronized (this) {
                        if (MusicPlayer.this.isPlaying()) {
                            MusicPlayer.this.mMediaPlayer.stop();
                            try {
                                MusicPlayer.this.mMediaPlayer.prepare();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return;
                }
                MusicPlayer.this.safeSetVolume(1.0f - (((float) (currentTimeMillis - j)) / ((float) i)));
                this.mHandler.postDelayed(this, 50);
            }
        }

        public void cancel() {
            this.mCancel = true;
            this.mHandler.removeCallbacks(this);
            synchronized (this) {
                if (MusicPlayer.this.isPlaying()) {
                    MusicPlayer.this.mMediaPlayer.stop();
                    try {
                        MusicPlayer.this.mMediaPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
