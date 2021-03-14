package com.devchie.photoeditor.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.devchie.photoeditor.adapter.ColorAdapter;
import com.devchie.photoeditor.dialog.DiscardDialog;
import com.devchie.photoeditor.eventbus.EditerModel;
import com.devchie.photoeditor.fragment.ColorFragment;
import com.devchie.photoeditor.fragment.EmojiFragment;
import com.devchie.photoeditor.fragment.OverlaysFragment;
import com.devchie.photoeditor.fragment.Overplay.ChristmasFragment;
import com.devchie.photoeditor.fragment.Overplay.FoodFragment;
import com.devchie.photoeditor.fragment.Overplay.LoveFragment;
import com.devchie.photoeditor.fragment.Overplay.MotivationFragment;
import com.devchie.photoeditor.fragment.Overplay.PhrasesFragment;
import com.devchie.photoeditor.fragment.Overplay.SayingsFragment;
import com.devchie.photoeditor.fragment.Overplay.SummerFragment;
import com.devchie.photoeditor.fragment.Overplay.TravelFragment;
import com.devchie.photoeditor.fragment.Overplay.WinterFragment;
import com.devchie.photoeditor.fragment.Sticker.StickerFragment;
import com.devchie.photoeditor.fragment.TuneImage.TuneFragment;
import com.devchie.photoeditor.fragment.texttools.FontFragment;
import com.devchie.photoeditor.fragment.texttools.FormatTextFragment;
import com.devchie.photoeditor.fragment.texttools.HightLightTextFragment;
import com.devchie.photoeditor.fragment.texttools.SpacingTextFragment;
import com.devchie.photoeditor.fragment.texttools.StrokeTextFragment;
import com.devchie.photoeditor.interfaces.ColorFragmentListener;
import com.devchie.photoeditor.interfaces.FontFragmentListener;
import com.devchie.photoeditor.interfaces.FormatTextFragmentListener;
import com.devchie.photoeditor.interfaces.HightLightFragmentListener;
import com.devchie.photoeditor.interfaces.OverlayListener;
import com.devchie.photoeditor.interfaces.ShadowFragmentListener;
import com.devchie.photoeditor.interfaces.SpacingFragmentListener;
import com.devchie.photoeditor.interfaces.StrokeFragmentListener;
import com.devchie.photoeditor.photoeditor.OnPhotoEditorListener;
import com.devchie.photoeditor.photoeditor.PhotoEditor;
import com.devchie.photoeditor.photoeditor.PhotoEditorView;
import com.devchie.photoeditor.photoeditor.SaveSettings;
import com.devchie.photoeditor.photoeditor.ViewType;
import com.devchie.photoeditor.utils.BitmapProcess;
import com.devchie.photoeditor.utils.ViewAnimation;
import com.devchie.photoeditor.view.RoundFrameLayout;
import com.devchie.photoeditor.view.StrokeTextView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.activities.BaseSplitActivity;
import com.devchie.videomaker.helper.ColorFilterGenerator;
import com.devchie.videomaker.view.RoundViewDelegate;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditPhotoActivity extends BaseSplitActivity implements View.OnClickListener, OnPhotoEditorListener,
        FormatTextFragmentListener, FontFragmentListener,
        HightLightFragmentListener, ColorFragmentListener, SpacingFragmentListener,
        StrokeFragmentListener, ShadowFragmentListener, EmojiFragment.EmojiListener,
        StickerFragment.StickerFragmentListener, OverlayListener, OverlaysFragment.NewOverlaysFragmentListener,
        TuneFragment.TuneFragmentListener {//TextEditorDialog.TextFragmentListener,
    public static Typeface defaultTypeface;

    public int addTextTabIndex = 0;
    private RoundFrameLayout border;
    private int brightnessFinal = 0;
    private ImageView btnAddText;
    private ImageView btnBack;
    private ImageView btnBackTextTools;
    private ImageView btnColorPicker;
    private ImageView btnFinish;
    private ImageView btnRedo;
    private ImageView btnUndo;
    private ColorAdapter colorAdapter;
    private int colorBackground;
    private int colorStroke = Color.parseColor("#000000");
    private int colorTextShadow = Color.parseColor("#000000");
    private int constrantFinal = 1;
    private int countMain = 0;
    private int countOverplay = 0;
    private int countPhoto = 0;
    private int countText = 0;

    public int current_tabTool = 0;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private int hueFinal = 1;
    private View imageClose;

    public ImageView imageViewMain;
    private PhotoEditorView imgPhotoEditor;
    Intent intent = null;
    ArrayList<String> listPhoto = new ArrayList<>();
    private ChristmasFragment mChristmasFragment;
    private FoodFragment mFoodFragment;
    private LoveFragment mLoveFragment;
    private MotivationFragment mMotivationFragment;

    public PhotoEditor mPhotoEditor;
    private PhrasesFragment mPhrasesFragment;
    private SayingsFragment mSayingFragment;
    private SummerFragment mSummerFragment;
    private TravelFragment mTravelFragment;
    private WinterFragment mWinterFragment;
    private Bitmap mainBitmap;

    public int numberAddedView;
    private String opticalBackground = "66";
    private int opticalText = 255;
    int position = 0;
    private ProgressBar progressBarLoading;
    private ProgressDialog progressDialog;
    private int rRadius = 0;


    private int rY = 0;
    private RecyclerView recyclerPhotoColor;

    public Bitmap resourceGraphic;
    private RelativeLayout rlEdit;
    private RelativeLayout rl_color_photo;

    public RelativeLayout rl_main_tool;
    private LinearLayout rl_photo_tools;
    private RelativeLayout rl_text_tool;
    private int saturationFinal = 1;
    private SeekBar sbRotatePhoto;
    private SeekBar sbTranparencyPhoto;
    private int styleText;
    private TabLayout tabLayoutTextTools;

    public TabLayout tablayoutImageTools;
    private StrokeTextView textViewMain;
    private Typeface typeface;

    public View viewMain;
    private ViewPager viewPagerImageTools;
    private ViewPager viewPagerTextTools;
    private int widthDevice;
    private int widthStroke = 1;
    private String[] tabs = {"Add text","Overlays","Emoji","Sticker","Tune"};

    private EditText edtQuotes;
    private Button btnDone;
    public void onEmoticons() {
    }

    public void onFitness() {
    }

    public void onGeometry() {
    }

    public void onHalloween() {
    }

    public void onNative() {
    }

    public void onStartViewChangeListener(ViewType viewType) {
    }

    public void onStopViewChangeListener(ViewType viewType) {
    }

    private ViewPagerCustomAdapter  mViewPagerAdapter = null;
    private ViewPagerCustomAdapter  mViewTextPagerAdapter = null;

    public void onCreate(Bundle bundle) {
        //activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
        EditPhotoActivity.super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_edit_photo);
        Log.d("XXXXXX", "Time1 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis));
        long currentThreadTimeMillis2 = SystemClock.currentThreadTimeMillis();
        addControls();
        addPhotoColor();
        setImageTranparency();
        Log.d("XXXXXX", "Time2 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis2));
        long currentThreadTimeMillis3 = SystemClock.currentThreadTimeMillis();
        defaultTypeface = Typeface.createFromAsset(getAssets(), "font/san_regular.ttf");
        PhotoEditor build = new PhotoEditor.Builder(this, this.imgPhotoEditor).setPinchTextScalable(true).setDefaultEmojiTypeface(defaultTypeface).build();
        this.mPhotoEditor = build;
        build.setOnPhotoEditorListener(this);
        Log.d("XXXXXX", "Time3 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis3));
        long currentThreadTimeMillis4 = SystemClock.currentThreadTimeMillis();
        getData();
        Log.d("XXXXXX", "Time4 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis4));
        long currentThreadTimeMillis5 = SystemClock.currentThreadTimeMillis();
        //setupViewPager(this.viewPagerTextTools);
        //this.tabLayoutTextTools.setupWithViewPager(this.viewPagerTextTools);
        //setupViewPagerImageTools(this. );
        this.tablayoutImageTools.setupWithViewPager(this.viewPagerImageTools);
        Log.d("XXXXXX", "Time5 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis5));
        long currentThreadTimeMillis6 = SystemClock.currentThreadTimeMillis();
        //setupTabIconsTextTool();
        //setupTabIconsImageTool();
        Log.d("XXXXXX", "Time6 " + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis6));
        SystemClock.currentThreadTimeMillis();

        edtQuotes = (EditText)findViewById(R.id.edtQuotes) ;

       btnDone = (Button)findViewById(R.id.btn_done) ;


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditPhotoActivity.this.mPhotoEditor.addText(edtQuotes.getText().toString(), EditPhotoActivity.this.getResources().getColor(R.color.white));
                hideKeyboard(EditPhotoActivity.this);

            }
        });
        initTabBar();

        initTextToolsTabbar();
    }

    @Override
    protected void onResume() {
        /*if (current_tabTool == 0) {
            viewPagerImageTools.setVisibility(View.GONE);
            edtQuotes.setVisibility(View.VISIBLE);
        }
        else {
            viewPagerImageTools.setVisibility(View.VISIBLE);
            edtQuotes.setVisibility(View.GONE);

        }*/
        super.onResume();
    }

    int[] tabsIcons = {
            R.drawable.ic_add_text,
            R.drawable.ic_overplay,
            R.drawable.ic_emoji,
            R.drawable.ic_add_sticker,
            R.drawable.ic_tune};

    int[] tabsTextTools = {
            R.drawable.ic_font,
            R.drawable.ic_format,
            R.drawable.ic_color_f,
            R.drawable.ic_stroke,
            R.drawable.ic_hightlight,
             R.drawable.ic_spacing};

    private final String[] tabText = {"Font","Format","Color","Stroke","Hightlight","Spacing"};

    private void initTextToolsTabbar(){

        mViewTextPagerAdapter = new ViewPagerCustomAdapter(fragmentManager);


        FontFragment fontFragment = new FontFragment();
        fontFragment.setListener(this);

        mViewTextPagerAdapter.addFragment(tabText[0], fontFragment);

        FormatTextFragment formatTextFragment = new FormatTextFragment();
        formatTextFragment.setListener(this);

        mViewTextPagerAdapter.addFragment(tabText[1],formatTextFragment);

        ColorFragment colorFragment = new ColorFragment();
        colorFragment.setListener(this);
        mViewTextPagerAdapter.addFragment(tabText[2],
                colorFragment
        );

        StrokeTextFragment strokeTextFragment = new StrokeTextFragment();
        strokeTextFragment.setListener(this);
        mViewTextPagerAdapter.addFragment(tabText[3],
                strokeTextFragment
        );


        HightLightTextFragment hightLightTextFragment = new HightLightTextFragment();
        hightLightTextFragment.setListener(this);
        mViewTextPagerAdapter.addFragment(tabText[4],
                hightLightTextFragment
        );

        if (Build.VERSION.SDK_INT >= 21) {
            SpacingTextFragment spacingTextFragment = new SpacingTextFragment();
            mViewTextPagerAdapter.addFragment(tabText[5],
                    spacingTextFragment
            );
            spacingTextFragment.setListener(this);
        }


        viewPagerTextTools.setAdapter(mViewTextPagerAdapter);
        if (Build.VERSION.SDK_INT >= 21) {
            viewPagerTextTools.setOffscreenPageLimit(6);
        } else {
            viewPagerTextTools.setOffscreenPageLimit(5);
        }

        tabLayoutTextTools.setupWithViewPager(viewPagerTextTools);

        //Init
        for (int i = 0; i <  tabLayoutTextTools.getTabCount();i++) {
            Drawable drawable = ContextCompat.getDrawable(this, tabsTextTools[i]);
            drawable.clearColorFilter();
            tabLayoutTextTools.getTabAt(i).setIcon(tabsTextTools[i]);
            if(i == 0 ) tabLayoutTextTools.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(EditPhotoActivity.this
                    , R.color.blue), PorterDuff.Mode.SRC_IN);
        }

        tabLayoutTextTools.setTabTextColors(getResources().getColor(R.color.black),getResources().getColor(R.color.blue));

        tabLayoutTextTools.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab.getIcon().setTint(getResources().getColor(R.color.blue));
                int tabIconColor = ContextCompat.getColor(EditPhotoActivity.this, R.color.blue);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab.getIcon().setTint(getResources().getColor(R.color.black));
                int tabIconColor = ContextCompat.getColor(EditPhotoActivity.this, R.color.black);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPagerTextTools.setCurrentItem(0);
        //tablayoutImageTools.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.blue));



    }
    private void initTabBar(){
        mViewPagerAdapter = new ViewPagerCustomAdapter(fragmentManager);

        mViewPagerAdapter.addFragment(tabs[0],
                new Fragment());

        OverlaysFragment overlaysFragment = new OverlaysFragment();
        overlaysFragment.setListener(this);

        mViewPagerAdapter.addFragment(tabs[1],overlaysFragment);

        EmojiFragment emojiFragment = new EmojiFragment();
        emojiFragment.setEmojiListener(this);
        mViewPagerAdapter.addFragment(tabs[2],
                emojiFragment
        );

        StickerFragment stickerFragment = new StickerFragment();
        stickerFragment.setStickerFragmentListener(this);
        mViewPagerAdapter.addFragment(tabs[3],
                stickerFragment
        );


        TuneFragment tuneFragment = new TuneFragment();
        tuneFragment.setTuneFragmentListener(this);
        mViewPagerAdapter.addFragment(tabs[4],
                tuneFragment
        );
        viewPagerImageTools.setAdapter(mViewPagerAdapter);
        viewPagerImageTools.setOffscreenPageLimit(5);
        tablayoutImageTools.setupWithViewPager(viewPagerImageTools);
        viewPagerImageTools.setCurrentItem(0);

        //Init
        for (int i = 0; i <  tablayoutImageTools.getTabCount();i++) {
            Drawable drawable = ContextCompat.getDrawable(this, tabsIcons[i]);
            drawable.clearColorFilter();
            tablayoutImageTools.getTabAt(i).setIcon(tabsIcons[i]);
        }

        tablayoutImageTools.setTabTextColors(getResources().getColor(R.color.black),getResources().getColor(R.color.blue));

        tablayoutImageTools.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.blue));
        tablayoutImageTools.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.blue));
                current_tabTool = tab.getPosition();
                if (tab.getPosition() == 0) {
                    viewPagerImageTools.setVisibility(View.GONE);
                    edtQuotes.setVisibility(View.VISIBLE);
                }
                else {
                    viewPagerImageTools.setVisibility(View.VISIBLE);
                    edtQuotes.setVisibility(View.GONE);

                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.black));

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setImageTranparency() {
        this.sbTranparencyPhoto.setMax(255);
        this.sbTranparencyPhoto.setProgress(255);
        this.sbTranparencyPhoto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (EditPhotoActivity.this.imageViewMain != null) {
                    EditPhotoActivity.this.imageViewMain.setImageAlpha(i);
                }
            }
        });
        this.sbRotatePhoto.setMax(360);
        this.sbRotatePhoto.setProgress(0);
        this.sbRotatePhoto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (EditPhotoActivity.this.viewMain != null) {
                    EditPhotoActivity.this.viewMain.setRotation((float) i);
                }
            }
        });
    }

    private void addPhotoColor() {
        this.recyclerPhotoColor.setHasFixedSize(true);
        this.recyclerPhotoColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ColorAdapter colorAdapter2 = new ColorAdapter(this, new ColorAdapter.ColorAdapterListener() {
            public void onColorItemSelected(int i) {
                BitmapProcess.changeBitmapColor(EditPhotoActivity.this.resourceGraphic, EditPhotoActivity.this.imageViewMain, i);
            }
        });
        this.colorAdapter = colorAdapter2;
        this.recyclerPhotoColor.setAdapter(colorAdapter2);
        this.btnColorPicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(EditPhotoActivity.this).setTitle("Select color").wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setPositiveButton((CharSequence) "OK", (ColorPickerClickListener) new ColorPickerClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                        BitmapProcess.changeBitmapColor(EditPhotoActivity.this.resourceGraphic, EditPhotoActivity.this.imageViewMain, i);
                    }
                }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).build().show();
            }
        });
    }



    private void getData() {
        Intent intent2 = getIntent();
        this.intent = intent2;
        this.listPhoto = intent2.getStringArrayListExtra("PHOTO");
        int intExtra = this.intent.getIntExtra("POSITION", 0);
        this.position = intExtra;
        if (intExtra >= this.listPhoto.size()) {
            finish();
        } else {
            Glide.with(this).load(this.listPhoto.get(this.position)).into(this.imgPhotoEditor.getSource());
        }
    }


    class DeCodeImageTask extends AsyncTask<Void, Void, Void> {

        public Void doInBackground(Void... voidArr) {
            return null;
        }

        DeCodeImageTask() {
        }
    }

    private void addControls() {
        this.btnUndo = (ImageView) findViewById(R.id.btnUndo);
        this.btnBack = (ImageView) findViewById(R.id.btnBackPhoto);
        this.btnRedo = (ImageView) findViewById(R.id.btnRedo);
        this.btnFinish = (ImageView) findViewById(R.id.btnFinishPhoto);
        this.btnBackTextTools = (ImageView) findViewById(R.id.btnBackTextTools);
        this.btnAddText = (ImageView) findViewById(R.id.btnAddText_toolbar);
        this.rl_text_tool = (RelativeLayout) findViewById(R.id.rl_text_tool);
        this.rl_main_tool = (RelativeLayout) findViewById(R.id.rl_main_tool);
        this.rl_photo_tools = (LinearLayout) findViewById(R.id.ll_photo_tool);
        this.rl_color_photo = (RelativeLayout) findViewById(R.id.rl_color_photo);
        this.rlEdit = (RelativeLayout) findViewById(R.id.relativeEdit);
        this.imgPhotoEditor = (PhotoEditorView) findViewById(R.id.imgPhotoEditor);
        this.viewPagerTextTools = findViewById(R.id.viewpagerTextTools);
        this.tabLayoutTextTools = findViewById(R.id.tablayoutTextTools);
        this.viewPagerImageTools = findViewById(R.id.viewpagerImageTools);
        this.tablayoutImageTools = findViewById(R.id.tablayoutImageTools);
        this.progressBarLoading = (ProgressBar) findViewById(R.id.progress_circular_loading);
        ChristmasFragment christmasFragment = new ChristmasFragment();
        this.mChristmasFragment = christmasFragment;
        christmasFragment.setOverlayListener(this);
        FoodFragment foodFragment = new FoodFragment();
        this.mFoodFragment = foodFragment;
        foodFragment.setOverlayListener(this);
        LoveFragment loveFragment = new LoveFragment();
        this.mLoveFragment = loveFragment;
        loveFragment.setOverlayListener(this);
        MotivationFragment motivationFragment = new MotivationFragment();
        this.mMotivationFragment = motivationFragment;
        motivationFragment.setOverlayListener(this);
        PhrasesFragment phrasesFragment = new PhrasesFragment();
        this.mPhrasesFragment = phrasesFragment;
        phrasesFragment.setOverlayListener(this);
        SayingsFragment sayingsFragment = new SayingsFragment();
        this.mSayingFragment = sayingsFragment;
        sayingsFragment.setOverlayListener(this);
        SummerFragment summerFragment = new SummerFragment();
        this.mSummerFragment = summerFragment;
        summerFragment.setOverlayListener(this);
        TravelFragment travelFragment = new TravelFragment();
        this.mTravelFragment = travelFragment;
        travelFragment.setOverlayListener(this);
        WinterFragment winterFragment = new WinterFragment();
        this.mWinterFragment = winterFragment;
        winterFragment.setOverlayListener(this);
        this.btnBack.setOnClickListener(this);
        this.btnBackTextTools.setOnClickListener(this);
        this.btnUndo.setOnClickListener(this);
        this.btnRedo.setOnClickListener(this);
        this.btnFinish.setOnClickListener(this);
        this.imgPhotoEditor.setOnClickListener(this);
        this.btnAddText.setOnClickListener(this);
        this.recyclerPhotoColor = findViewById(R.id.recycler_color_photo);
        this.sbTranparencyPhoto = (SeekBar) findViewById(R.id.seekbar_photo_transparency);
        this.sbRotatePhoto = (SeekBar) findViewById(R.id.seekbar_rotate);
        this.btnColorPicker = (ImageView) findViewById(R.id.btn_picker_color_photo);
    }

    @SuppressLint("ResourceType")
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.imgPhotoEditor) {
            switch (id) {
                case R.id.btnAddText_toolbar:
                    if (this.numberAddedView < 6) {
                        this.mPhotoEditor.addText(getString(R.string.doubletap), getResources().getColor(17170443));
                        return;
                    } else {
                        Toast.makeText(this, R.string.maxtext, Toast.LENGTH_SHORT).show();
                        return;
                    }
                case R.id.btnBackPhoto:
                    DiscardDialog discardDialog = new DiscardDialog(this);
                    discardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    try {
                        discardDialog.show();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case R.id.btnBackTextTools:
                    if (this.countMain == 0) {
                        ViewAnimation.animationView(this.rl_main_tool);
                        this.rl_text_tool.setVisibility(View.GONE);
                        this.rl_photo_tools.setVisibility(View.GONE);
                        this.tablayoutImageTools.getTabAt(0).select();
                        this.countText = 0;
                        this.countMain++;
                        this.countOverplay = 0;
                        this.countPhoto = 0;
                        return;
                    }
                    return;
                default:
                    switch (id) {
                        case R.id.btnFinishPhoto:
                            savePhoto();
                            return;
                        case R.id.btnRedo:
                            this.mPhotoEditor.redo();
                            return;
                        case R.id.btnUndo:
                            this.mPhotoEditor.undo();
                            return;
                        default:
                            return;
                    }
            }
        } else if (this.countMain == 0) {
            this.mPhotoEditor.clearHelperBox();
            ViewAnimation.animationView(this.rl_main_tool);
            this.rl_text_tool.setVisibility(View.GONE);
            this.rl_photo_tools.setVisibility(View.GONE);
            if (this.current_tabTool == 0) {
                this.current_tabTool = 1;
            }
            this.tablayoutImageTools.getTabAt(this.current_tabTool).select();
            this.countText = 0;
            this.countMain++;
            this.countPhoto = 0;
            this.countOverplay = 0;
        }
    }

    private void savePhoto() {
        File outputMediaFile = getOutputMediaFile();
        try {
            outputMediaFile.createNewFile();


            SaveSettings build = new SaveSettings.Builder().setClearViewsEnabled(true).setTransparencyEnabled(true).build();
            if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                this.mPhotoEditor.saveAsFile(outputMediaFile.getAbsolutePath(), build, new PhotoEditor.OnSaveListener() {
                    public void onFailure(Exception exc) {
                       String a = "";
                    }

                    public void onSuccess(String str) {
                       /* EditPhotoActivity.this.listPhoto.set(EditPhotoActivity.this.position, str);
                        EditPhotoActivity.this.intent.putStringArrayListExtra("AFTER", EditPhotoActivity.this.listPhoto);
                        EditPhotoActivity editPhotoActivity = EditPhotoActivity.this;
                        editPhotoActivity.setResult(115, editPhotoActivity.intent);*/

                        /*Intent resultData = new Intent();
                        resultData.putExtra(MyConstant.NEW_URL, str);
                        resultData.putExtra(MyConstant.POSITION,position);
                        setResult(RESULT_OK, resultData);*/
                        EventBus.getDefault().post(new EditerModel(str,position));

                        EditPhotoActivity.this.finish();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getOutputMediaFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/TextPhoto");
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        return new File(file.getPath() + File.separator + ("MI_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".jpg"));
    }

    public void onEditTextChangeListener(View view, String str, int i) {
        //showTextEditorFragment(str);
        Log.d("","");
    }

  /*  private void showTextEditorFragment(String str) {
        TextEditorDialog textEditorDialog = new TextEditorDialog(this, str);
        textEditorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                View currentFocus = EditPhotoActivity.this.getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) EditPhotoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                if (EditPhotoActivity.this.rl_main_tool != null && EditPhotoActivity.this.rl_main_tool.getVisibility() == View.VISIBLE) {
                    EditPhotoActivity.this.tablayoutImageTools.getTabAt(0).select();
                }
            }
        });
        textEditorDialog.setTextListener(this);
        textEditorDialog.show();
    }*/

    public void onClickGetEditTextChangeListener(StrokeTextView strokeTextView, RoundFrameLayout roundFrameLayout) {
        this.textViewMain = strokeTextView;
        this.border = roundFrameLayout;
        edtQuotes.setVisibility(View.VISIBLE);

        if (this.countText == 0) {
            ViewAnimation.animationView(this.rl_text_tool);
            this.rl_main_tool.setVisibility(View.GONE);
            this.rl_photo_tools.setVisibility(View.GONE);
            this.countMain = 0;
            this.countText++;
            this.countPhoto = 0;
            this.countOverplay = 0;
        }
    }

    public void onClickGetImageViewListener(ImageView imageView, View view) {
        this.imageViewMain = imageView;
        this.viewMain = view;
        if (this.countPhoto == 0) {
            ViewAnimation.animationView(this.rl_photo_tools);
            this.rl_main_tool.setVisibility(View.GONE);
            this.rl_text_tool.setVisibility(View.GONE);
            this.rl_color_photo.setVisibility(View.GONE);
            this.countPhoto++;
            this.countMain = 0;
            this.countText = 0;
            this.countOverplay = 0;
        }
    }

    public void onAddViewListener(ViewType viewType, int i) {
        this.numberAddedView = i;
    }

    public void onRemoveViewListener(int i) {
        this.numberAddedView = i;
    }

    public void onRemoveViewListener(ViewType viewType, int i) {
        ViewAnimation.animationView(this.rl_main_tool);
        this.rl_color_photo.setVisibility(View.GONE);
        this.rl_text_tool.setVisibility(View.GONE);
        this.rl_photo_tools.setVisibility(View.GONE);
        Log.d("@@", "current_tabTool " + this.current_tabTool + " addTextTabIndex " + this.addTextTabIndex);
        if (this.current_tabTool == this.addTextTabIndex) {
            this.current_tabTool = 0;
        }
        this.tablayoutImageTools.getTabAt(this.current_tabTool).select();
        this.countText = 0;
        this.countMain++;
        this.countOverplay = 0;
        this.countPhoto = 0;
    }

    public void onBackPressed() {
        DiscardDialog discardDialog = new DiscardDialog(this);
        discardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        discardDialog.show();
    }

    @SuppressLint("WrongConstant")
    public void onTextAlign(int i) {
        if (i == 1) {
            this.textViewMain.setGravity(3);
        } else if (i == 2) {
            this.textViewMain.setGravity(17);
        } else if (i == 3) {
            this.textViewMain.setGravity(5);
            StrokeTextView strokeTextView = this.textViewMain;
            strokeTextView.setTypeface(strokeTextView.getTypeface(), 2);
        }
    }

    @SuppressLint("WrongConstant")
    public void onTextStyle(int i) {
        switch (i) {
            case 1:
                StrokeTextView strokeTextView = this.textViewMain;
                strokeTextView.setTypeface(strokeTextView.getTypeface(), 3);
                this.styleText = i;
                return;
            case 2:
                StrokeTextView strokeTextView2 = this.textViewMain;
                strokeTextView2.setTypeface(strokeTextView2.getTypeface(), 1);
                this.styleText = i;
                return;
            case 3:
                StrokeTextView strokeTextView3 = this.textViewMain;
                strokeTextView3.setTypeface(strokeTextView3.getTypeface(), 2);
                this.styleText = i;
                return;
            case 4:
                StrokeTextView strokeTextView4 = this.textViewMain;
                strokeTextView4.setTypeface(Typeface.create(strokeTextView4.getTypeface(), 0));
                this.styleText = i;
                return;
            case 5:
                this.textViewMain.setAllCaps(true);
                return;
            case 6:
                this.textViewMain.setAllCaps(false);
                return;
            default:
                return;
        }
    }

    public void onTextSize(int i) {
        this.textViewMain.setTextSize((float) i);
        Log.d("TEXTTTTT", "onTextSize " + i);
    }

    public void onTextPadding(int i) {
        this.border.setPadding(i, i, i, i);
    }

    @SuppressLint("WrongConstant")
    public void onFontSelected(String str) {
        AssetManager assets = getAssets();
        Typeface createFromAsset = Typeface.createFromAsset(assets, "font/" + str);
        this.typeface = createFromAsset;
        int i = this.styleText;
        if (i == 1) {
            this.textViewMain.setTypeface(createFromAsset, 3);
        } else if (i == 2) {
            this.textViewMain.setTypeface(createFromAsset, 1);
        } else if (i == 3) {
            this.textViewMain.setTypeface(createFromAsset, 2);
        } else if (i != 4) {
            this.textViewMain.setTypeface(createFromAsset, 0);
        } else {
            this.textViewMain.setTypeface(createFromAsset, 0);
        }
    }

    public void onHightLightColorSelected(int i) {
        this.colorBackground = i;
        String format = String.format("%06X", new Object[]{Integer.valueOf(i & 16777215)});
        RoundViewDelegate delegate = this.border.getDelegate();
        delegate.setBackgroundColor(Color.parseColor("#" + this.opticalBackground + format));
    }

    public void onHightLightColorOpacityChangeListerner(String str) {
        String format = String.format("%06X", new Object[]{Integer.valueOf(this.colorBackground & 16777215)});
        this.opticalBackground = str;
        RoundViewDelegate delegate = this.border.getDelegate();
        delegate.setBackgroundColor(Color.parseColor("#" + this.opticalBackground + format));
    }

    public void onHighLightRadius(int i) {
        this.border.getDelegate().setCornerRadius(i);
    }

    public void onColorSelected(int i) {
        this.colorTextShadow = i;
        this.textViewMain.getPaint().setShader((Shader) null);
        this.textViewMain.setTextColor(i);
        StrokeTextView strokeTextView = this.textViewMain;
        strokeTextView.setTextColor(strokeTextView.getTextColors().withAlpha(this.opticalText));
    }

    public void onColorOpacityChangeListerner(int i) {
        this.opticalText = i;
        StrokeTextView strokeTextView = this.textViewMain;
        strokeTextView.setTextColor(strokeTextView.getTextColors().withAlpha(i));
    }

    public void onLineHeight(int i) {
        setLineGeight(this.textViewMain, i);
    }

    private void setLineGeight(TextView textView, int i) {
        textView.setLineSpacing((float) (dpToPixel((float) i) - textView.getPaint().getFontMetricsInt((Paint.FontMetricsInt) null)), 1.0f);
    }

    public int dpToPixel(float f) {
        return (int) (f * (((float) getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public void onSpacingLetter(float f) {
        this.textViewMain.setLetterSpacing(f);
    }

    public void onStrokeColorSelected(int i) {
        this.colorStroke = i;
        this.textViewMain.setStrokeColor(i);
    }

    public void onStrokeWidthChangeListener(int i) {
        this.widthStroke = i;
        this.textViewMain.setStrokeWidth(i);
    }

    public void onDyShadowChangeListener(int i) {
        this.rY = i;
        this.textViewMain.setStrokeWidth(0);
        float f = (float) i;
        this.textViewMain.setShadowLayer((float) this.rRadius, f, f, this.colorTextShadow);
    }

    public void onRadiusChangeListener(int i) {
        this.rRadius = i;
        this.textViewMain.setStrokeWidth(0);
        int i2 = this.rY;
        this.textViewMain.setShadowLayer((float) this.rRadius, (float) i2, (float) i2, this.colorTextShadow);
    }

    public void onShadowColorSelected(int i) {
        this.colorTextShadow = i;
        this.textViewMain.setStrokeWidth(0);
        int i2 = this.rY;
        this.textViewMain.setShadowLayer((float) this.rRadius, (float) i2, (float) i2, this.colorTextShadow);
    }

    public void onEmojiClick(String str) {
        if (this.numberAddedView < 6) {
            this.mPhotoEditor.addEmoji(str);
        } else {
            Toast.makeText(this, R.string.max_item, Toast.LENGTH_SHORT).show();
        }
    }

    public void onStickerFragmentClick(Bitmap bitmap) {
        if (this.numberAddedView < 6) {
            this.mPhotoEditor.addImage(bitmap);
        } else {
            Toast.makeText(this, R.string.max_item, Toast.LENGTH_SHORT).show();
        }
    }

    public void onOverplayClick(@NotNull Bitmap bitmap) {
        int i;
        int i2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            i2 = (int) ((300.0f / ((float) width)) * ((float) height));
            i = (int) 300.0f;
        } else {
            i = (int) ((300.0f / ((float) height)) * ((float) width));
            i2 = (int) 300.0f;
        }
        if (this.numberAddedView < 6) {
            this.mPhotoEditor.addImage2(Bitmap.createScaledBitmap(bitmap, i, i2, false));
        } else {
            Toast.makeText(this, R.string.max_item, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBrightnessChosse(int i) {
        this.brightnessFinal = i;
        this.imgPhotoEditor.getSource().setColorFilter(ColorFilterGenerator.adjustColor(this.brightnessFinal, this.saturationFinal, this.constrantFinal, this.hueFinal));
    }

    public void onConstrastChosse(int i) {
        this.constrantFinal = i;
        this.imgPhotoEditor.getSource().setColorFilter(ColorFilterGenerator.adjustColor(this.brightnessFinal, this.saturationFinal, this.constrantFinal, this.hueFinal));
    }

    public void onHueChosee(int i) {
        this.hueFinal = i;
        this.imgPhotoEditor.getSource().setColorFilter(ColorFilterGenerator.adjustColor(this.brightnessFinal, this.saturationFinal, this.constrantFinal, this.hueFinal));
    }

    public void onSaturationChosse(int i) {
        this.saturationFinal = i;
        this.imgPhotoEditor.getSource().setColorFilter(ColorFilterGenerator.adjustColor(this.brightnessFinal, this.saturationFinal, this.constrantFinal, this.hueFinal));
    }

    public void onClickGetBitmapOverlay(Bitmap bitmap) {
        this.resourceGraphic = bitmap;
    }

    public void onClickGetGraphicViewListener(ImageView imageView, View view, View view2) {
        this.imageViewMain = imageView;
        this.imageClose = view2;
        this.viewMain = view;
        if (this.countOverplay == 0) {
            ViewAnimation.animationView(this.rl_photo_tools);
            this.rl_main_tool.setVisibility(View.GONE);
            this.rl_text_tool.setVisibility(View.GONE);
            this.rl_color_photo.setVisibility(View.VISIBLE);
            this.countPhoto = 0;
            this.countOverplay++;
            this.countMain = 0;
            this.countText = 0;
        }
    }

    public void onAdded(StrokeTextView strokeTextView, RoundFrameLayout roundFrameLayout) {
        this.textViewMain = strokeTextView;
        this.border = roundFrameLayout;
    }

    public void onText(String str) {
        this.textViewMain.setText(str);
        if (this.countText == 0) {
            ViewAnimation.animationView(this.rl_text_tool);
            this.rl_main_tool.setVisibility(View.GONE);
            this.rl_photo_tools.setVisibility(View.GONE);
            this.countMain = 0;
            this.countText++;
            this.countPhoto = 0;
            this.countOverplay = 0;
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onOverlaysFragmentClick(Bitmap bitmap) {
        int i;
        int i2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            i2 = (int) ((300.0f / ((float) width)) * ((float) height));
            i = (int) 300.0f;
        } else {
            i = (int) ((300.0f / ((float) height)) * ((float) width));
            i2 = (int) 300.0f;
        }
        if (this.numberAddedView < 6) {
            this.mPhotoEditor.addImage2(Bitmap.createScaledBitmap(bitmap, i, i2, false));
        } else {
            Toast.makeText(this, R.string.max_item, Toast.LENGTH_SHORT).show();
        }
    }

}
