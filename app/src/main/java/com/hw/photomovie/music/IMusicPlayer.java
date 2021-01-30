package com.hw.photomovie.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import com.hw.photomovie.music.MusicPlayer;
import java.io.FileDescriptor;

public interface IMusicPlayer {
    boolean isPlaying();

    void pause();

    void release();

    void seekTo(int i);

    void setDataSource(Context context, Uri uri, MusicPlayer.OnMusicOKListener onMusicOKListener);

    void setDataSource(AssetFileDescriptor assetFileDescriptor);

    void setDataSource(FileDescriptor fileDescriptor);

    void setDataSource(String str);

    void setErrorListener(MediaPlayer.OnErrorListener onErrorListener);

    void setLooping(boolean z);

    void start();

    void stop();
}
