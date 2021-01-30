package com.devchie.videomaker.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.devchie.videomaker.ads.FacebookAds;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.util.GmsVersion;
import com.google.android.material.tabs.TabLayout;
import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.PhotoMovieFactory;
import com.hw.photomovie.PhotoMoviePlayer;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.model.SimplePhotoData;
import com.hw.photomovie.music.MusicPlayer;
import com.hw.photomovie.record.GLMovieRecorder;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.GLTextureMovieRender;
import com.hw.photomovie.render.GLTextureView;
import com.hw.photomovie.timer.IMovieTimer;
import com.hw.photomovie.util.MLog;
import com.devchie.videomaker.R;
import com.devchie.videomaker.UriUtil;
import com.devchie.videomaker.adaper.ViewPagerAdapter;
import com.devchie.videomaker.ads.AdmobAds;
import com.devchie.videomaker.dialog.PhotoDiscardDialog;
import com.devchie.videomaker.fragment.Movie.DurationFragment;
import com.devchie.videomaker.fragment.Movie.FilterFragment;
import com.devchie.videomaker.fragment.Movie.MusicFragment;
import com.devchie.videomaker.fragment.Movie.RatioFragment;
import com.devchie.videomaker.fragment.Movie.TransferFragment;
import com.devchie.videomaker.helper.DeviceUtils;
import com.devchie.videomaker.model.FilterItem;
import com.devchie.videomaker.model.TransferItem;
import com.devchie.videomaker.view.PlayPauseView;
import com.devchie.videomaker.view.radioview.RatioDatumMode;
import com.devchie.videomaker.view.radioview.RatioFrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class MovieActivity extends BaseSplitActivity implements View.OnClickListener, IMovieTimer.MovieListener, FilterFragment.FilterFragmentListener, TransferFragment.TransferFragmentListener, MusicFragment.MusicFragmentListener, DurationFragment.DurationFragmentListener, RatioFragment.RatioFragmentListener {
    private static final int REQUEST_MUSIC = 234;

    public static final String TAG = MovieActivity.class.getSimpleName();
    private Runnable alphaRunnable = new Runnable() {
        public void run() {
            MovieActivity.this.controlContainer.animate().alpha(0.0f).setListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    MovieActivity.this.controlContainer.setVisibility(View.GONE);
                }
            }).start();
        }
    };
    String audioPath = null;
    ImageView btnBack;
    ImageView btnFinish;
    PlayPauseView btnPlayPause;

    public ViewGroup controlContainer;
    int duration = 2000;
    private Handler handler = new Handler();
    private boolean isSeekBarTracking = false;
    private AdView mAdview;
    private GLTextureView mGLTextureView;
    private GLSurfaceMovieRenderer mMovieRenderer;
    private PhotoMovieFactory.PhotoMovieType mMovieType = PhotoMovieFactory.PhotoMovieType.GRADIENT;
    private Uri mMusicUri;
    private PhotoMovie mPhotoMovie;

    public PhotoMoviePlayer mPhotoMoviePlayer;
    String musicPath = null;
    String path = null;

    public ProgressDialog progressDialog;
    RatioFrameLayout ratioFrameLayout;

    public ViewGroup savingLayout;
    private SeekBar sbControl;
    private TabLayout tabLayoutMovie;
    private TextView tvControlCurrentTime;
    private TextView tvControlTotalTime;

    public TextView tvSaving;
    private ViewPager viewPagerMovie;

    private void startToFinalActivity() {
    }

    public void onMovieEnd() {
    }

    public void onMoviePaused() {
    }

    public void onMovieResumed() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_movie);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading...");
        this.progressDialog.setCanceledOnTouchOutside(false);
        showAd();
        initView();
        this.btnPlayPause.setPlayPauseListener(new PlayPauseView.PlayPauseListener() {
            public void play() {
                MovieActivity.this.onResumeVideo();
            }

            public void pause() {
                MovieActivity.this.onPauseVideo();
            }
        });
        ArrayList<String> stringArrayList = getIntent().getExtras().getStringArrayList("PHOTO");
        int[] iArr = {R.raw.love, R.raw.friend, R.raw.beach, R.raw.travel, R.raw.christmas, R.raw.happy, R.raw.movie, R.raw.positive, R.raw.summer};
        for (int i = 0; i < 9; i++) {
            this.path = Environment.getExternalStorageDirectory() + "/StoryMaker";
            File file = new File(this.path);
            if (file.mkdirs() || file.isDirectory()) {
                String str = i + ".m4r";
                try {
                    CopyRAWtoSDCard(iArr[i], this.path + File.separator + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        getMusicData(0);
        initMoviePlayer();
        onPhotoPick(stringArrayList);
    }

    private void showAd() {
        AdmobAds.loadBanner(this);

    }

    private void initView() {
        this.ratioFrameLayout = (RatioFrameLayout) findViewById(R.id.videoContainer);
        this.mGLTextureView = (GLTextureView) findViewById(R.id.gl_texture);
        this.btnPlayPause = (PlayPauseView) findViewById(R.id.btn_play_pause);
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        this.btnBack = imageView;
        imageView.setOnClickListener(this);
        ImageView imageView2 = (ImageView) findViewById(R.id.btnFinish);
        this.btnFinish = imageView2;
        imageView2.setOnClickListener(this);
        this.mGLTextureView.setOnClickListener(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerMovie);
        this.viewPagerMovie = viewPager;
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutMovie);
        this.tabLayoutMovie = tabLayout;
        tabLayout.setupWithViewPager(this.viewPagerMovie);
        setupTabIconMovie();
        this.tabLayoutMovie.setTabTextColors(getResources().getColor(R.color.un_selected_white), getResources().getColor(R.color.white));
        this.btnPlayPause.setPlaying(true);
        this.controlContainer = (ViewGroup) findViewById(R.id.control_container);
        this.sbControl = (SeekBar) findViewById(R.id.sb_control);
        this.tvControlCurrentTime = (TextView) findViewById(R.id.tv_control_current_time);
        this.tvControlTotalTime = (TextView) findViewById(R.id.tv_control_total_time);
        this.sbControl.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        this.savingLayout = (ViewGroup) findViewById(R.id.saving_layout);
        this.tvSaving = (TextView) findViewById(R.id.tv_saving);
        hideControl();
    }

    private void setupTabIconMovie() {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, (ViewGroup) null);
        textView.setText(getString(R.string.str_filter));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_movie_filter, 0, 0);
        this.tabLayoutMovie.getTabAt(0).setCustomView((View) textView);
        TextView textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, (ViewGroup) null);
        textView2.setText(getString(R.string.str_transfer));
        textView2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_movie_transfer, 0, 0);
        this.tabLayoutMovie.getTabAt(1).setCustomView((View) textView2);
        TextView textView3 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, (ViewGroup) null);
        textView3.setText(getString(R.string.str_music));
        textView3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_movie_music, 0, 0);
        this.tabLayoutMovie.getTabAt(2).setCustomView((View) textView3);
        TextView textView4 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, (ViewGroup) null);
        textView4.setText(getString(R.string.str_duration));
        textView4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_duration_movie, 0, 0);
        this.tabLayoutMovie.getTabAt(3).setCustomView((View) textView4);
        TextView textView5 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, (ViewGroup) null);
        textView5.setText(getString(R.string.str_ratio));
        textView5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_aspect_ratio, 0, 0);
        this.tabLayoutMovie.getTabAt(4).setCustomView((View) textView5);
        if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            this.tabLayoutMovie.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        FilterFragment filterFragment = new FilterFragment();
        viewPagerAdapter.addFrag(filterFragment, getString(R.string.str_filter));
        filterFragment.setFilterFragmentListener(this);
        TransferFragment transferFragment = new TransferFragment();
        viewPagerAdapter.addFrag(transferFragment, getString(R.string.str_transfer));
        transferFragment.setTransferFragmentListener(this);
        MusicFragment musicFragment = new MusicFragment();
        viewPagerAdapter.addFrag(musicFragment, getString(R.string.str_music));
        musicFragment.setMusicFragmentListener(this);
        DurationFragment durationFragment = new DurationFragment();
        viewPagerAdapter.addFrag(durationFragment, getString(R.string.str_duration));
        durationFragment.setDurationFragmentListener(this);
        RatioFragment ratioFragment = new RatioFragment();
        viewPagerAdapter.addFrag(ratioFragment, getString(R.string.str_ratio));
        ratioFragment.RatioFragmentListener(this);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initMoviePlayer() {
        this.mMovieRenderer = new GLTextureMovieRender(this.mGLTextureView);
        PhotoMoviePlayer photoMoviePlayer = new PhotoMoviePlayer(getApplicationContext());
        this.mPhotoMoviePlayer = photoMoviePlayer;
        photoMoviePlayer.setMovieRenderer(this.mMovieRenderer);
        this.mPhotoMoviePlayer.setMovieListener(this);
        this.mPhotoMoviePlayer.setLoop(true);
        this.mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            public void onPreparing(PhotoMoviePlayer photoMoviePlayer, float f) {
            }

            public void onPrepared(PhotoMoviePlayer photoMoviePlayer, int i, int i2) {
                MovieActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MovieActivity.this.mPhotoMoviePlayer.start();
                    }
                });
            }

            public void onError(PhotoMoviePlayer photoMoviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
    }

    public void onPhotoPick(ArrayList<String> arrayList) {
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(new SimplePhotoData(this, it.next(), 2));
        }
        PhotoSource photoSource = new PhotoSource(arrayList2);
        if (this.mPhotoMoviePlayer == null) {
            startPlay(photoSource);
        } else {
            createVideo(photoSource, PhotoMovieFactory.PhotoMovieType.GRADIENT);
        }
    }


    public void startVideo() {
        this.mPhotoMoviePlayer.start();
        int duration2 = this.mPhotoMovie.getDuration() / 1000;
        this.tvControlTotalTime.setText(DeviceUtils.getTimeString(duration2));
        this.sbControl.setMax(duration2);
    }

    private File initVideoFile() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory.mkdirs();
        }
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory = getCacheDir();
        }
        return new File(externalStoragePublicDirectory, String.format("Story_maker_%s.mp4", new Object[]{new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Long.valueOf(System.currentTimeMillis()))}));
    }

    public void onPauseVideo() {
        this.mPhotoMoviePlayer.pause();
    }

    public void onResumeVideo() {
        this.mPhotoMoviePlayer.start();
    }

    private void startPlay(PhotoSource photoSource) {
        PhotoMovie generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, this.mMovieType, this.duration);
        this.mPhotoMovie = generatePhotoMovie;
        this.mPhotoMoviePlayer.setDataSource(generatePhotoMovie);
        this.mPhotoMoviePlayer.prepare();
    }

    public void saveVideo() {
        this.mPhotoMoviePlayer.pause();

        AdmobAds.loadNativeAds(this, (View) null);
        FacebookAds.loadNativeAd(this);
        this.savingLayout.setVisibility(View.VISIBLE);
        TextView textView = this.tvSaving;
        textView.setText(getString(R.string.str_saving_video) + "...");
        final long currentTimeMillis = System.currentTimeMillis();
        GLMovieRecorder gLMovieRecorder = new GLMovieRecorder(this);
        final File initVideoFile = initVideoFile();
        MLog.i(TAG, "saveVideo initVideoFile");
        gLMovieRecorder.configOutput(this.mGLTextureView.getWidth(), this.mGLTextureView.getHeight(), this.mGLTextureView.getWidth() * this.mGLTextureView.getHeight() > 1500000 ? GmsVersion.VERSION_SAGA : 4000000, 30, 1, initVideoFile.getAbsolutePath());
        PhotoMovie generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(this.mPhotoMovie.getPhotoSource(), this.mMovieType, this.duration);
        GLSurfaceMovieRenderer gLSurfaceMovieRenderer = new GLSurfaceMovieRenderer(this.mMovieRenderer);
        gLSurfaceMovieRenderer.setPhotoMovie(generatePhotoMovie);
        if (this.mMusicUri != null) {
            this.audioPath = this.musicPath;
        }
        if (!TextUtils.isEmpty(this.audioPath)) {
            if (Build.VERSION.SDK_INT < 18) {
                Toast.makeText(getApplicationContext(), "Mix audio needs api18!", Toast.LENGTH_LONG).show();
            } else {
                gLMovieRecorder.setMusic(this.audioPath);
            }
        }
        gLMovieRecorder.setDataSource(gLSurfaceMovieRenderer);
        MLog.i(TAG, "saveVideo setDataSource");
        final GLMovieRecorder gLMovieRecorder2 = gLMovieRecorder;
        gLMovieRecorder.startRecord(new GLMovieRecorder.OnRecordListener() {
            public void onRecordFinish(boolean z) {
                File file = initVideoFile;
                long currentTimeMillis = System.currentTimeMillis();
                String access$100 = MovieActivity.TAG;
                MLog.i(access$100, "onRecordFinish:" + (currentTimeMillis - currentTimeMillis));
                if (z) {
                    MovieActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(initVideoFile)));
                    Intent intent = new Intent(MovieActivity.this, FinalActivity.class);
                    intent.putExtra("VIDEO", Uri.fromFile(file));
                    MovieActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(MovieActivity.this.getApplicationContext(), "Record error!", Toast.LENGTH_LONG).show();
                }
                if (gLMovieRecorder2.getAudioRecordException() != null) {
                    Log.e("XXXXXXXX", gLMovieRecorder2.getAudioRecordException().getMessage());
                    Context applicationContext = MovieActivity.this.getApplicationContext();
                    Toast.makeText(applicationContext, "record audio failed:" + gLMovieRecorder2.getAudioRecordException().toString(), Toast.LENGTH_LONG).show();
                }
                MovieActivity.this.savingLayout.setVisibility(View.GONE);
            }

            public void onRecordProgress(final int i, final int i2) {
                MovieActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView access$300 = MovieActivity.this.tvSaving;
                        int per = ((int) ((((float) i) / ((float) i2)) * 100.0f));
                        if (per > 100) {
                            per = 100;
                        }
                        access$300.setText(MovieActivity.this.getString(R.string.str_saving_video) + " " + per + "%");
                    }
                });
            }
        });
    }

    public void setMusic(Uri uri) {
        this.mMusicUri = uri;
        createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
    }

    public void createVideo(PhotoSource photoSource, PhotoMovieFactory.PhotoMovieType photoMovieType) {
        this.progressDialog.show();
        this.mPhotoMoviePlayer.stop();
        PhotoMovie generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, photoMovieType, this.duration);
        this.mPhotoMovie = generatePhotoMovie;
        this.mPhotoMoviePlayer.setDataSource(generatePhotoMovie);
        this.mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            public void onPreparing(PhotoMoviePlayer photoMoviePlayer, float f) {
                Log.d("onPrepare", "onPreparing " + f);
            }

            public void onPrepared(PhotoMoviePlayer photoMoviePlayer, int i, int i2) {
                Log.d("onPrepare", "onPrepared " + i2);
                MovieActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MovieActivity.this.startVideo();
                        if (MovieActivity.this.progressDialog != null) {
                            MovieActivity.this.progressDialog.dismiss();
                        }
                    }
                });
            }

            public void onError(PhotoMoviePlayer photoMoviePlayer) {
                if (MovieActivity.this.progressDialog != null) {
                    MovieActivity.this.progressDialog.dismiss();
                }
                Log.d("onPrepare", "onPrepare onError");
            }
        });
        if (this.mMusicUri != null) {
            ProgressDialog progressDialog2 = this.progressDialog;
            if (progressDialog2 != null) {
                progressDialog2.show();
            }
            this.mPhotoMoviePlayer.setMusic(this, this.mMusicUri, new MusicPlayer.OnMusicOKListener() {
                public void onMusicOK() {
                    MovieActivity.this.mPhotoMoviePlayer.prepare();
                }
            });
        }
    }


    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            PhotoDiscardDialog photoDiscardDialog = new PhotoDiscardDialog(this);
            photoDiscardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            photoDiscardDialog.show();
        } else if (id == R.id.btnFinish) {
            saveVideo();
            AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
        } else if (id == R.id.gl_texture) {
            this.handler.removeCallbacks(this.alphaRunnable);
            this.controlContainer.setAlpha(1.0f);
            this.controlContainer.setVisibility(View.VISIBLE);
            hideControl();
        }
    }

    private void hideControl() {
        this.handler.removeCallbacks(this.alphaRunnable);
        this.handler.postDelayed(this.alphaRunnable, 3500);
    }

    public void onFilter(FilterItem filterItem) {
        this.mMovieRenderer.setMovieFilter(filterItem.initFilter());
    }

    public void onMyMusic() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, REQUEST_MUSIC);
    }

    public void onTypeMusic(int i) {
        getMusicData(i);
        setMusic(this.mMusicUri);
    }

    private void getMusicData(int i) {
        switch (i) {
            case 0:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689479");
                this.musicPath = this.path + File.separator + "0.m4r";
                return;
            case 1:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689475");
                this.musicPath = this.path + File.separator + "1.m4r";
                return;
            case 2:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689473");
                this.musicPath = this.path + File.separator + "4.m4r";
                return;
            case 3:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689481");
                this.musicPath = this.path + File.separator + "7.m4r";
                return;
            case 4:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689480");
                this.musicPath = this.path + File.separator + "6.m4r";
                return;
            case 5:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689472");
                this.musicPath = this.path + File.separator + "2.m4r";
                return;
            case 6:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689482");
                this.musicPath = this.path + File.separator + "8.m4r";
                return;
            case 7:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689483");
                this.musicPath = this.path + File.separator + "3.m4r";
                return;
            case 8:
                this.mMusicUri = Uri.parse("android.resource://com.devchie.videomaker/2131689476");
                this.musicPath = this.path + File.separator + "5.m4r";
                return;
            default:
                return;
        }
    }

    public void onTransfer(TransferItem transferItem) {
        this.mMovieType = transferItem.type;
        createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
    }

    public void onDurationSelect(int i) {
        this.duration = i;
        createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
    }

    public void onMovieUpdate(int i) {
        if (!this.isSeekBarTracking) {
            int round = Math.round(((float) i) / 1000.0f);
            this.sbControl.setProgress(round);
            this.tvControlCurrentTime.setText(DeviceUtils.getTimeString(round));
        }
    }

    public void onMovieStarted() {
        MLog.d(TAG, "onMovieStarted");
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == REQUEST_MUSIC) {
            setMusic(intent.getData());
            this.musicPath = UriUtil.getPath(this, this.mMusicUri);
        }
    }


    public void onPostResume() {
        super.onPostResume();
    }


    public void onPause() {
        super.onPause();
    }


    public void onStop() {
        super.onStop();
        PhotoMoviePlayer photoMoviePlayer = this.mPhotoMoviePlayer;
        if (photoMoviePlayer != null) {
            photoMoviePlayer.pause();
        }
    }


    public void onStart() {
        super.onStart();
        PhotoMoviePlayer photoMoviePlayer = this.mPhotoMoviePlayer;
        if (photoMoviePlayer != null) {
            photoMoviePlayer.start();
        }
    }

    public void onBackPressed() {
        PhotoDiscardDialog photoDiscardDialog = new PhotoDiscardDialog(this);
        photoDiscardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        photoDiscardDialog.show();
    }

    public void destroyMovie() {
        PhotoMoviePlayer photoMoviePlayer = this.mPhotoMoviePlayer;
        if (photoMoviePlayer != null) {
            photoMoviePlayer.pause();
            this.mPhotoMoviePlayer.destroy();
        }
    }

    public void onRatio(int i) {
        if (i == 11) {
            this.ratioFrameLayout.setRatio(RatioDatumMode.valueOf(1), 1.0f, 1.0f);
            createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
        } else if (i == 34) {
            this.ratioFrameLayout.setRatio(RatioDatumMode.valueOf(1), 3.0f, 4.0f);
            createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
        } else if (i == 43) {
            this.ratioFrameLayout.setRatio(RatioDatumMode.valueOf(1), 4.0f, 3.0f);
            createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
        } else if (i == 169) {
            this.ratioFrameLayout.setRatio(RatioDatumMode.valueOf(1), 16.0f, 9.0f);
            createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
        } else if (i == 916) {
            this.ratioFrameLayout.setRatio(RatioDatumMode.valueOf(1), 9.0f, 16.0f);
            createVideo(this.mPhotoMovie.getPhotoSource(), this.mMovieType);
        }
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
