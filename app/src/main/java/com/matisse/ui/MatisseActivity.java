/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.matisse.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.adaper.PhotoAdapter;


import com.devchie.videomaker.R;
import com.devchie.videomaker.helper.MyConstant;
import com.devchie.videomaker.listener.ItemClickListener;
import com.devchie.videomaker.model.Photo;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.matisse.TopSheet.TopSheetBehavior;
import com.matisse.internal.entity.Album;
import com.matisse.internal.entity.Item;
import com.matisse.internal.entity.SelectionSpec;
import com.matisse.internal.model.AlbumCollection;
import com.matisse.internal.model.SelectedItemCollection;
import com.matisse.internal.ui.AlbumPreviewActivity;
import com.matisse.internal.ui.BasePreviewActivity;
import com.matisse.internal.ui.MediaSelectionFragment;
import com.matisse.internal.ui.SelectedPreviewActivity;
import com.matisse.internal.ui.adapter.AlbumMediaAdapter;
import com.matisse.internal.ui.adapter.AlbumsAdapter;
import com.matisse.internal.ui.widget.AlbumsSpinner;
import com.matisse.internal.ui.widget.CheckRadioView;
import com.matisse.internal.ui.widget.IncapableDialog;
import com.matisse.internal.utils.MediaStoreCompat;
import com.matisse.internal.utils.PathUtils;
import com.matisse.internal.utils.PhotoMetadataUtils;
import com.matisse.internal.utils.SingleMediaScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity to display albums and media content (images/videos) in each album
 * and also support media selecting operations.
 */
public class MatisseActivity extends AppCompatActivity implements
        AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener,
        MediaSelectionFragment.SelectionProvider, View.OnClickListener,
        AlbumMediaAdapter.CheckStateListener, AlbumMediaAdapter.OnMediaClickListener,
        AlbumMediaAdapter.OnPhotoCapture {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";
    public static final String EXTRA_RESULT_ORIGINAL_ENABLE = "extra_result_original_enable";
    private static final int REQUEST_CODE_PREVIEW = 23;
    private static final int REQUEST_CODE_CAPTURE = 24;
    public static final String CHECK_STATE = "checkState";
    private final AlbumCollection mAlbumCollection = new AlbumCollection();
    private MediaStoreCompat mMediaStoreCompat;
    private SelectedItemCollection mSelectedCollection = new SelectedItemCollection(this);
    private SelectionSpec mSpec;

    private AlbumsSpinner mAlbumsSpinner;
    private AlbumsAdapter mAlbumsAdapter;
    private PhotoAdapter mPhotoAdapter;
    private TextView mButtonPreview;
    private TextView mButtonApply;
    private View mContainer;
    private View mEmptyView;
    private View mAlbumTopSheet;
    private View mSelectedAlbumView;
    private View listPhoto;

    private LinearLayout mOriginalLayout;
    private CheckRadioView mOriginal;
    private RecyclerView mRcvPhoto;
    private RecyclerView mRcvAlbum;
    private boolean mOriginalEnable;

    private int mTopSheetState = TopSheetBehavior.STATE_COLLAPSED;
    private TopSheetBehavior<View> mTopSheetBehavior;

    private ArrayList<Photo> mSelectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // programmatically set theme before super.onCreate()
        mSpec = SelectionSpec.getInstance();
        setTheme(mSpec.themeId);
        super.onCreate(savedInstanceState);
        if (!mSpec.hasInited) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        setContentView(R.layout.activity_matisse);

        if (mSpec.needOrientationRestriction()) {
            setRequestedOrientation(mSpec.orientation);
        }

        if (mSpec.capture) {
            mMediaStoreCompat = new MediaStoreCompat(this);
            if (mSpec.captureStrategy == null)
                throw new RuntimeException("Don't forget to set CaptureStrategy.");
            mMediaStoreCompat.setCaptureStrategy(mSpec.captureStrategy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        TypedArray ta = getTheme().obtainStyledAttributes(new int[]{R.attr.album_element_color});
        int color = ta.getColor(0, 0);
        ta.recycle();
        navigationIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        mButtonPreview = (TextView) findViewById(R.id.button_preview);
        listPhoto = findViewById(R.id.listPhoto);
        listPhoto.setVisibility(View.GONE);
        mButtonApply = (TextView) findViewById(R.id.button_apply);
        mButtonPreview.setOnClickListener(this);
        mButtonApply.setOnClickListener(this);
        mContainer = findViewById(R.id.container);
        mEmptyView = findViewById(R.id.empty_view);
        mOriginalLayout = findViewById(R.id.originalLayout);
        mOriginal = findViewById(R.id.original);
        mOriginalLayout.setOnClickListener(this);
        mRcvPhoto = findViewById(R.id.recyclerPhoto);
        mRcvAlbum = findViewById(R.id.albumList);
        mAlbumTopSheet = findViewById(R.id.top_sheet);

        mSelectedCollection.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mOriginalEnable = savedInstanceState.getBoolean(CHECK_STATE);
        }
        updateBottomToolbar();

        mAlbumsAdapter = new AlbumsAdapter(this, null, false);
        mAlbumsSpinner = new AlbumsSpinner(this);
        mAlbumsSpinner.setOnItemSelectedListener(this);
        mAlbumsSpinner.setSelectedTextView((TextView) findViewById(R.id.selected_album));
        //mAlbumsSpinner.setPopupAnchorView(findViewById(R.id.toolbar));
        mAlbumsSpinner.setAdapter(mAlbumsAdapter);
        mAlbumCollection.onCreate(this, this);
        mAlbumCollection.onRestoreInstanceState(savedInstanceState);
        mAlbumCollection.loadAlbums();

        View topSheetWrapper = findViewById(R.id.topSheetWrapper);
        mSelectedAlbumView =  findViewById(R.id.selected_album);
        mTopSheetBehavior = TopSheetBehavior.from(mAlbumTopSheet);
        mSelectedAlbumView.setOnClickListener(view -> {
            if (mTopSheetState == TopSheetBehavior.STATE_COLLAPSED) {
                mTopSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
                mTopSheetState = TopSheetBehavior.STATE_EXPANDED;
                topSheetWrapper.setBackgroundColor(Color.parseColor("#40000000"));
            } else {
                if (mTopSheetState == TopSheetBehavior.STATE_EXPANDED) {
                    mTopSheetBehavior.setState(TopSheetBehavior.STATE_COLLAPSED);
                    mTopSheetState = TopSheetBehavior.STATE_COLLAPSED;
                    topSheetWrapper.setBackground(null);
                }
            }
        });
        findViewById(R.id.blackBar).setOnClickListener(view -> mSelectedAlbumView.callOnClick());
        findViewById(R.id.movie_add_float).setOnClickListener(view -> {
            if (mSelectedCollection == null || mSelectedCollection.count() < 1) return;
            Intent intent = new Intent(this, ActivityEditSelectedPhoto.class);
            Bundle imageBundle = new Bundle();
            imageBundle.putParcelableArrayList(MyConstant.KEY_SELECTED_IMAGES, new ArrayList<>(mSelectedCollection.asList()));
            intent.putExtras(imageBundle);
            startActivityForResult(intent, MyConstant.REQUEST_CODE_EDIT_IMAGES);
        });
        mTopSheetBehavior.setTopSheetCallback(new TopSheetBehavior.TopSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == TopSheetBehavior.STATE_COLLAPSED){
                    mTopSheetState = TopSheetBehavior.STATE_COLLAPSED;
                    topSheetWrapper.setBackground(null);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset, @Nullable Boolean isOpening) {
            }
        });

        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();
        dragDropManager.setInitiateOnLongPress(true);
        dragDropManager.setInitiateOnMove(false);

        mPhotoAdapter = new PhotoAdapter(mSelectedPhotos, this, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

            }

            @Override
            public void onItemDeleteClick(View view, int i) {
                //remove item at bottom photo recycler view  & selection list
                long id = mSelectedPhotos.get(i).id;
                List<Item> items = mSelectedCollection.asList();
                for (int j = items.size() - 1; j >= 0; j--) {
                    if (items.get(j).id == id) {
                        mSelectedCollection.remove(items.get(j));
                        break;
                    }
                }
                mSelectedPhotos.remove(i);
                mPhotoAdapter.notifyItemRemoved(i);
                //update selection list on view
                Fragment mediaSelectionFragment = getSupportFragmentManager().findFragmentByTag(
                        MediaSelectionFragment.class.getSimpleName());
                if (mediaSelectionFragment instanceof MediaSelectionFragment) {
                    ((MediaSelectionFragment) mediaSelectionFragment).refreshMediaGrid();
                }
            }
        });
        mPhotoAdapter.setHasStableIds(true);
        mRcvPhoto.setAdapter(dragDropManager.createWrappedAdapter(mPhotoAdapter));
        dragDropManager.attachRecyclerView(mRcvPhoto);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectedCollection.onSaveInstanceState(outState);
        mAlbumCollection.onSaveInstanceState(outState);
        outState.putBoolean("checkState", mOriginalEnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlbumCollection.onDestroy();
        mSpec.onCheckedListener = null;
        mSpec.onSelectedListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_PREVIEW) {
            Bundle resultBundle = data.getBundleExtra(BasePreviewActivity.EXTRA_RESULT_BUNDLE);
            ArrayList<Item> selected = resultBundle.getParcelableArrayList(SelectedItemCollection.STATE_SELECTION);
            mOriginalEnable = data.getBooleanExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, false);
            int collectionType = resultBundle.getInt(SelectedItemCollection.STATE_COLLECTION_TYPE,
                    SelectedItemCollection.COLLECTION_UNDEFINED);
            if (data.getBooleanExtra(BasePreviewActivity.EXTRA_RESULT_APPLY, false)) {
                Intent result = new Intent();
                ArrayList<Uri> selectedUris = new ArrayList<>();
                ArrayList<String> selectedPaths = new ArrayList<>();
                if (selected != null) {
                    for (Item item : selected) {
                        selectedUris.add(item.getContentUri());
                        selectedPaths.add(PathUtils.getPath(this, item.getContentUri()));
                    }
                }
                result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selectedUris);
                result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPaths);
                result.putExtra(EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable);
                setResult(RESULT_OK, result);
                finish();
            } else {
                mSelectedCollection.overwrite(selected, collectionType);
                Fragment mediaSelectionFragment = getSupportFragmentManager().findFragmentByTag(
                        MediaSelectionFragment.class.getSimpleName());
                if (mediaSelectionFragment instanceof MediaSelectionFragment) {
                    ((MediaSelectionFragment) mediaSelectionFragment).refreshMediaGrid();
                }
                updateBottomToolbar();
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE) {
            // Just pass the data back to previous calling Activity.
            Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
            String path = mMediaStoreCompat.getCurrentPhotoPath();
            ArrayList<Uri> selected = new ArrayList<>();
            selected.add(contentUri);
            ArrayList<String> selectedPath = new ArrayList<>();
            selectedPath.add(path);
            Intent result = new Intent();
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selected);
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPath);
            setResult(RESULT_OK, result);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                MatisseActivity.this.revokeUriPermission(contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            new SingleMediaScanner(this.getApplicationContext(), path, new SingleMediaScanner.ScanListener() {
                @Override public void onScanFinish() {
                    Log.i("SingleMediaScanner", "scan finish!");
                }
            });
            finish();
        }else {
            if (requestCode == MyConstant.REQUEST_CODE_EDIT_IMAGES) {
                List<Item> items = data.getParcelableArrayListExtra(MyConstant.KEY_SELECTED_IMAGES);
                if (items != null){
                    mSelectedCollection.overwrite(new ArrayList<>(items), mSelectedCollection.getCollectionType());
                    mSelectedPhotos.clear();
                    for (int i = 0; i < items.size(); i++){
                        mSelectedPhotos.add(new Photo(
                                items.get(i).id,
                                PathUtils.getPath(this, items.get(i).getContentUri()))
                        );
                    }

                    Fragment mediaSelectionFragment = getSupportFragmentManager().findFragmentByTag(
                            MediaSelectionFragment.class.getSimpleName());
                    if (mediaSelectionFragment instanceof MediaSelectionFragment) {
                        ((MediaSelectionFragment) mediaSelectionFragment).refreshMediaGrid();
                    }

                    mPhotoAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void updateBottomToolbar() {

        int selectedCount = mSelectedCollection.count();
        if (selectedCount == 0) {
            listPhoto.setVisibility(View.GONE);
            mButtonPreview.setEnabled(false);
            mButtonApply.setEnabled(false);
            mButtonApply.setText(getString(R.string.button_apply_default));
        } else if (selectedCount == 1 && mSpec.singleSelectionModeEnabled()) {
            listPhoto.setVisibility(View.VISIBLE);
            mButtonPreview.setEnabled(true);
            mButtonApply.setText(R.string.button_apply_default);
            mButtonApply.setEnabled(true);
        } else {
            listPhoto.setVisibility(View.VISIBLE);
            mButtonPreview.setEnabled(true);
            mButtonApply.setEnabled(true);
            mButtonApply.setText(getString(R.string.button_apply, selectedCount));
        }


        if (mSpec.originalable) {
            mOriginalLayout.setVisibility(View.VISIBLE);
            updateOriginalState();
        } else {
            mOriginalLayout.setVisibility(View.INVISIBLE);
        }


    }


    private void updateOriginalState() {
        mOriginal.setChecked(mOriginalEnable);
        if (countOverMaxSize() > 0) {

            if (mOriginalEnable) {
                IncapableDialog incapableDialog = IncapableDialog.newInstance("",
                        getString(R.string.error_over_original_size, mSpec.originalMaxSize));
                incapableDialog.show(getSupportFragmentManager(),
                        IncapableDialog.class.getName());

                mOriginal.setChecked(false);
                mOriginalEnable = false;
            }
        }
    }


    private int countOverMaxSize() {
        int count = 0;
        int selectedCount = mSelectedCollection.count();
        for (int i = 0; i < selectedCount; i++) {
            Item item = mSelectedCollection.asList().get(i);

            if (item.isImage()) {
                float size = PhotoMetadataUtils.getSizeInMB(item.size);
                if (size > mSpec.originalMaxSize) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_preview) {
            Intent intent = new Intent(this, SelectedPreviewActivity.class);
            intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.getDataWithBundle());
            intent.putExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable);
            startActivityForResult(intent, REQUEST_CODE_PREVIEW);
        } else if (v.getId() == R.id.button_apply) {
            Intent result = new Intent();
            ArrayList<Uri> selectedUris = (ArrayList<Uri>) mSelectedCollection.asListOfUri();
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selectedUris);
            ArrayList<String> selectedPaths = (ArrayList<String>) mSelectedCollection.asListOfString();
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPaths);
            result.putExtra(EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable);
            setResult(RESULT_OK, result);
            finish();
        } else if (v.getId() == R.id.originalLayout) {
            int count = countOverMaxSize();
            if (count > 0) {
                IncapableDialog incapableDialog = IncapableDialog.newInstance("",
                        getString(R.string.error_over_original_count, count, mSpec.originalMaxSize));
                incapableDialog.show(getSupportFragmentManager(),
                        IncapableDialog.class.getName());
                return;
            }

            mOriginalEnable = !mOriginalEnable;
            mOriginal.setChecked(mOriginalEnable);

            if (mSpec.onCheckedListener != null) {
                mSpec.onCheckedListener.onCheck(mOriginalEnable);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAlbumCollection.setStateCurrentSelection(position);
        mAlbumsAdapter.getCursor().moveToPosition(position);
        Album album = Album.valueOf(mAlbumsAdapter.getCursor());
        if (album.isAll() && SelectionSpec.getInstance().capture) {
            album.addCaptureCount();
        }
        onAlbumSelected(album);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAlbumLoad(final Cursor cursor) {
        mAlbumsAdapter.swapCursor(cursor);
        // select default album.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                ImageAlbumAdapter albumAdapter = new ImageAlbumAdapter(cursor, position -> {
                    mAlbumCollection.setStateCurrentSelection(position);
                    mAlbumsAdapter.getCursor().moveToPosition(position);
                    Album album = Album.valueOf(mAlbumsAdapter.getCursor());
                    if (album.isAll() && SelectionSpec.getInstance().capture) {
                        album.addCaptureCount();
                    }
                    onAlbumSelected(album);
                    mSelectedAlbumView.callOnClick();
                });
                mRcvAlbum.setAdapter(albumAdapter);

                cursor.moveToPosition(mAlbumCollection.getCurrentSelection());

                mAlbumsSpinner.setSelection(MatisseActivity.this,
                        mAlbumCollection.getCurrentSelection());

                Album album = Album.valueOf(cursor);
                if (album.isAll() && SelectionSpec.getInstance().capture) {
                    album.addCaptureCount();
                }
                onAlbumSelected(album);
            }
        });
    }

    @Override
    public void onAlbumReset() {
        mAlbumsAdapter.swapCursor(null);
    }

    private void onAlbumSelected(Album album) {
        if (album.isAll() && album.isEmpty()) {
            mContainer.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            ((TextView)mSelectedAlbumView).setText(album.getDisplayName(this));
            mContainer.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            Fragment fragment = MediaSelectionFragment.newInstance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, MediaSelectionFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onUpdate() {
        // notify bottom toolbar that check state changed.
        updateBottomToolbar();

        if (mSpec.onSelectedListener != null) {
            mSpec.onSelectedListener.onSelected(
                    mSelectedCollection.asListOfUri(), mSelectedCollection.asListOfString());
        }

        List<Item> items = mSelectedCollection.asList();
        int itemSize = items.size();
        int photoSize = mSelectedPhotos.size();
        if (photoSize < itemSize){//added new item
            //int photoSize = mSelectedPhotos.size();
            // for (int i = photoSize - 1; i < itemSize; i++){
            mSelectedPhotos.add(new Photo(
                    items.get(itemSize - 1).id,
                    PathUtils.getPath(this, items.get(itemSize - 1).getContentUri()))
            );
            // }
            mPhotoAdapter.notifyItemInserted(photoSize);
        } else {
            if (photoSize > itemSize){//deleted an item
                int deletedPos = photoSize - 1;
                boolean isDeleted;
                for (int i = 0; i < photoSize; i++){
                    isDeleted = true;
                    //check if updated item list contains this photo
                    for (int j = 0; j < itemSize; j++){
                        if (items.get(j).id == mSelectedPhotos.get(i).id){
                            isDeleted = false;
                            break;
                        }
                    }
                    if (isDeleted){
                        deletedPos = i;
                        break;
                    }
                }
                mSelectedPhotos.remove(deletedPos);
                mPhotoAdapter.notifyItemRemoved(deletedPos);
            }
        }
    }

    @Override
    public void onMediaClick(Album album, Item item, int adapterPosition) {
        Intent intent = new Intent(this, AlbumPreviewActivity.class);
        intent.putExtra(AlbumPreviewActivity.EXTRA_ALBUM, album);
        intent.putExtra(AlbumPreviewActivity.EXTRA_ITEM, item);
        intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.getDataWithBundle());
        intent.putExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable);
        startActivityForResult(intent, REQUEST_CODE_PREVIEW);
    }

    @Override
    public SelectedItemCollection provideSelectedItemCollection() {
        return mSelectedCollection;
    }

    @Override
    public void capture() {
        if (mMediaStoreCompat != null) {
            mMediaStoreCompat.dispatchCaptureIntent(this, REQUEST_CODE_CAPTURE);
        }
    }

}
