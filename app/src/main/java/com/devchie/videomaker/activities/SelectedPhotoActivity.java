package com.devchie.videomaker.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.devchie.videomaker.ads.FacebookAds;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.devchie.videomaker.R;
import com.devchie.videomaker.adaper.PhotoAdapter;
import com.devchie.videomaker.ads.AdmobAds;
import com.devchie.videomaker.listener.ItemClickListener;
import com.devchie.videomaker.model.Photo;
import com.devchie.videomaker.view.RoundRectView;
import com.devchie.photoeditor.activity.EditPhotoActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.lang.Thread;
import java.util.ArrayList;

public class SelectedPhotoActivity extends BaseSplitActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_SELECT = 100;
    private FloatingActionButton btnAddMore;
    private LinearLayout btnAddPhoto;
    private FloatingActionButton btnEditPhoto;
    private ViewGroup btnMovie;
    private int countAd = 1;
    private RecyclerViewDragDropManager dragMgr;
    private DrawerLayout drawerLayout;

    public ImageView imgPhoto;

    public ArrayList<Photo> listPhoto = new ArrayList<>();
    private LinearLayout llFacebook;
    private LinearLayout llGmail;
    private RoundRectView llHolderRecyclerView;
    private LinearLayout llPolicy;
    private LinearLayout llRate;
    private LinearLayout llshare;
    private AdView mAdview;
    private int maxImgCount = 30;
    private NavigationView navigationView;

    public String pathSelected;
    private Photo photo1;

    public PhotoAdapter photoAdapter;
    private ArrayList<String> photos = null;

    public Photo photoselected;

    public int positionSelected = 0;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerPhoto;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ArrayList<String> sendPhotos = new ArrayList<>();
    private Uri uriSelected = null;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_selected_photo);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
                Log.e("EXCEPTION_IN_THREAD", thread.getName() + " : " + th.getMessage());
            }
        });
        addControls();
        addRecyclerView();
        addPhoto();
        FacebookAds.loadBanner(this);
        AdmobAds.loadBanner(this);


    }

    private void addRecyclerView() {
        this.recyclerPhoto = findViewById(R.id.recyclerPhoto);
        RecyclerViewDragDropManager recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        this.dragMgr = recyclerViewDragDropManager;
        recyclerViewDragDropManager.setInitiateOnMove(false);
      /*  this.dragMgr.setInitiateOnLongPress(true);
        this.recyclerPhoto.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        PhotoAdapter photoAdapter2 = new PhotoAdapter(this.listPhoto, this, new ItemClickListener() {
            public void onItemClick(View view, int i) {
                SelectedPhotoActivity.this.positionSelected = i;
                SelectedPhotoActivity selectedPhotoActivity = SelectedPhotoActivity.this;
                selectedPhotoActivity.photoselected = (Photo) selectedPhotoActivity.listPhoto.get(i);
                SelectedPhotoActivity selectedPhotoActivity2 = SelectedPhotoActivity.this;
                selectedPhotoActivity2.pathSelected = selectedPhotoActivity2.photoselected.paths;
                RequestOptions requestOptions = new RequestOptions();
                ((requestOptions.centerCrop()).placeholder((int) R.drawable.__picker_ic_photo_black_48dp)).error((int) R.drawable.__picker_ic_broken_image_black_48dp);
                Glide.with(SelectedPhotoActivity.this).load(SelectedPhotoActivity.this.photoselected.paths).apply((BaseRequestOptions<?>) requestOptions).thumbnail(0.1f).into(SelectedPhotoActivity.this.imgPhoto);
            }

            public void onItemDeleteClick(View view, int i) {
                SelectedPhotoActivity.this.listPhoto.remove(i);
                SelectedPhotoActivity.this.photoAdapter.notifyDataSetChanged();
            }
        });
        this.photoAdapter = photoAdapter2;
        photoAdapter2.setHasStableIds(true);
        this.recyclerPhoto.setAdapter(this.dragMgr.createWrappedAdapter(this.photoAdapter));
        this.dragMgr.attachRecyclerView(this.recyclerPhoto);*/
    }

    private void addControls() {
        this.llHolderRecyclerView = (RoundRectView) findViewById(R.id.llRecyclerView);
        this.btnEditPhoto = (FloatingActionButton) findViewById(R.id.btnEditPhoto);
        this.btnMovie = (ViewGroup) findViewById(R.id.movie_add_float);
        this.btnAddMore = (FloatingActionButton) findViewById(R.id.photo_add_float);
        this.btnAddPhoto = (LinearLayout) findViewById(R.id.movie_add);
        this.imgPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        this.btnAddMore.setOnClickListener(this);
        this.btnAddPhoto.setOnClickListener(this);
        this.btnEditPhoto.setOnClickListener(this);
        this.btnMovie.setOnClickListener(this);
    }


    @SuppressLint("RestrictedApi")
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && intent != null && i == 100) {
            this.selectedPhotos.clear();
            ArrayList<String> arrayList = (ArrayList) Matisse.obtainPathResult(intent);
            this.selectedPhotos = arrayList;
            if (arrayList != null && !arrayList.isEmpty()) {
                if (intent != null) {
                    this.btnAddPhoto.setVisibility(View.GONE);
                    this.llHolderRecyclerView.setVisibility(View.VISIBLE);
                    this.btnAddMore.setVisibility(View.VISIBLE);
                    this.btnEditPhoto.setVisibility(View.VISIBLE);
                }
                for (int i3 = 0; i3 < this.selectedPhotos.size(); i3++) {
                    this.listPhoto.add(new Photo((long) i3, this.selectedPhotos.get(i3)));
                }
                this.photo1 = this.listPhoto.get(0);
                this.uriSelected = Uri.fromFile(new File(this.photo1.paths));
                RequestOptions requestOptions = new RequestOptions();
                ((RequestOptions) ((RequestOptions) requestOptions.centerCrop()).placeholder((int) R.drawable.__picker_ic_photo_black_48dp)).error((int) R.drawable.__picker_ic_broken_image_black_48dp);
                Glide.with((FragmentActivity) this).load(this.uriSelected).apply((BaseRequestOptions<?>) requestOptions).thumbnail(0.1f).into(this.imgPhoto);
                this.photoAdapter.notifyDataSetChanged();
            }
        }
        if (i == 113 && i2 == 115) {
            this.photos = intent.getStringArrayListExtra("AFTER");
            this.selectedPhotos.clear();
            this.sendPhotos.clear();
            this.listPhoto.clear();
            ArrayList<String> arrayList2 = this.photos;
            if (arrayList2 != null) {
                this.selectedPhotos.addAll(arrayList2);
                for (int i4 = 0; i4 < this.selectedPhotos.size(); i4++) {
                    this.listPhoto.add(new Photo((long) i4, this.selectedPhotos.get(i4)));
                }
            }
            this.photo1 = this.listPhoto.get(0);
            this.uriSelected = Uri.fromFile(new File(this.photo1.paths));
            RequestOptions requestOptions2 = new RequestOptions();
            ((RequestOptions) ((RequestOptions) requestOptions2.centerCrop()).placeholder((int) R.drawable.__picker_ic_photo_black_48dp)).error((int) R.drawable.__picker_ic_broken_image_black_48dp);
            Glide.with((FragmentActivity) this).load(this.uriSelected).apply((BaseRequestOptions<?>) requestOptions2).thumbnail(0.1f).into(this.imgPhoto);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditPhoto:
//                checkDynamicLibAndEditPhoto();
                gotoPhotoEditor();
                return;
            case R.id.movie_add:
                addPhoto();
                return;
            case R.id.movie_add_float:
                ProgressDialog progressDialog2 = new ProgressDialog(this);
                this.progressDialog = progressDialog2;
                progressDialog2.setMessage(getString(R.string.com_facebook_loading));
                this.progressDialog.setCanceledOnTouchOutside(false);
                this.progressDialog.show();
                this.countAd++;
                this.photoAdapter.notifyDataSetChanged();
                this.sendPhotos.clear();
                for (int i = 0; i < this.listPhoto.size(); i++) {
                    this.sendPhotos.add(this.listPhoto.get(i).paths);
                }
                createMovie();
                return;
            case R.id.photo_add_float:
                addPhoto();
                return;
            default:
                return;
        }
    }

    private void createMovie() {
        this.photoAdapter.notifyDataSetChanged();
        if (this.sendPhotos.size() < 3) {
            ProgressDialog progressDialog2 = this.progressDialog;
            if (progressDialog2 != null && progressDialog2.isShowing()) {
                this.progressDialog.dismiss();
            }
            Toast.makeText(this, getString(R.string.more_than_3_photos), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MovieActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("PHOTO", this.sendPhotos);
        intent.putExtras(bundle);
        startActivity(intent);
        if (!FacebookAds.showFullAds(null)) {
            AdmobAds.showFullAds(null);
        }
    }

    private void addPhoto() {

        Matisse.from((Activity) this).choose(MimeType.ofImage(), false).showSingleMediaType(true).theme(R.style.Matisse_Dracula).countable(true).maxSelectable(30).restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(new GlideEngine()).showPreview(false).forResult(100);
    }


    public void onPostResume() {
        super.onPostResume();
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }


    public void onStop() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            this.progressDialog.dismiss();
        }
        super.onStop();
    }


    public void gotoPhotoEditor() {
        Intent intent = new Intent(SelectedPhotoActivity.this, EditPhotoActivity.class);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.listPhoto.size(); i++) {
            arrayList.add(this.listPhoto.get(i).paths);
        }
        intent.putStringArrayListExtra("PHOTO", arrayList);
        intent.putExtra("POSITION", this.positionSelected);
        if (this.positionSelected < arrayList.size()) {
            startActivityForResult(intent, 113);
        }
    }


}
