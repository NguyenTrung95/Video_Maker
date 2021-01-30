package com.hw.photomovie.timer;

public interface IMovieTimer {

    public interface MovieListener {
        void onMovieEnd();

        void onMoviePaused();

        void onMovieResumed();

        void onMovieStarted();

        void onMovieUpdate(int i);
    }

    int getCurrentPlayTime();

    void pause();

    void setLoop(boolean z);

    void setMovieListener(MovieListener movieListener);

    void start();
}
