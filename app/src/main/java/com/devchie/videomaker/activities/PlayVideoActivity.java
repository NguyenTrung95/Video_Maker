package com.devchie.videomaker.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.dialog.RateDialog;

public class PlayVideoActivity extends BaseSplitActivity implements View.OnClickListener {

    public MediaController mediaController;
    Uri uri = null;

    public VideoView videoView;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play_video);
        addControls();
        getDataShowVideo();
    }

    private void getDataShowVideo() {
        this.uri = (Uri) getIntent().getParcelableExtra("VIDEO");
        if (this.mediaController == null) {
            MediaController mediaController2 = new MediaController(this);
            this.mediaController = mediaController2;
            mediaController2.setAnchorView(this.videoView);
            this.videoView.setMediaController(this.mediaController);
        }
        this.videoView.setVideoURI(this.uri);
        this.videoView.requestFocus();
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                PlayVideoActivity.this.videoView.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
                        PlayVideoActivity.this.mediaController.setAnchorView(PlayVideoActivity.this.videoView);
                    }
                });
            }
        });
    }

    private void addControls() {
        this.videoView = (VideoView) findViewById(R.id.videoView);
        findViewById(R.id.btnRate).setOnClickListener(this);
        findViewById(R.id.btnBackFinal).setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBackFinal) {
            super.onBackPressed();
        } else if (id == R.id.btnRate) {
            showDialog();
        }
    }

    private void showDialog() {
        new RateDialog(this, false).show();
    }


    public void onDestroy() {
        super.onDestroy();
    }
}
