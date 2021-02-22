package com.matisse.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.devchie.photoeditor.activity.EditPhotoActivity;
import com.devchie.videomaker.helper.MyConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.devchie.videomaker.R;
import com.matisse.internal.entity.Item;
import com.matisse.internal.utils.ImageEditCallback;
import com.matisse.internal.utils.PathUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditSelectedPhoto extends AppCompatActivity {

    private RecyclerView imageRcv;
    private TextView vCount;
    private ImageEditAdapter adapter;

    private String selectedString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_photo);

        imageRcv = findViewById(R.id.editSelectedPhotoList);
        vCount = findViewById(R.id.editSelectedPhotoCount);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View vDone = findViewById(R.id.editSelectedPhotoDone);
        View vBack = findViewById(R.id.editSelectedPhotoBack);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        }
        */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            onBackPressed();
            }
        );

        selectedString = getString(R.string.selected);

        initImageList();

        vDone.setOnClickListener(view -> {
            Intent resultData = new Intent();
            resultData.putParcelableArrayListExtra(MyConstant.KEY_SELECTED_IMAGES, new ArrayList<>(adapter.getItems()));
            setResult(RESULT_OK, resultData);
            finish();
        });
        vBack.setOnClickListener(view -> onBackPressed());
    }

    private void initImageList() {
        Bundle imageBundle = getIntent().getExtras();
        if (imageBundle != null) {
            List<Item> items = imageBundle.getParcelableArrayList(MyConstant.KEY_SELECTED_IMAGES);
            adapter = new ImageEditAdapter(items, new ImageEditAdapter.OnItemInteraction() {
                @Override
                public void onItemRemoved(Item item, int position) {
                    setCount(adapter.getItemCount());
                }

                @Override
                public void onEditClick(Item item, int position) {
                    toEditPhotoActivity();
                }
            });
            imageRcv.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ImageEditCallback(adapter));
            itemTouchHelper.attachToRecyclerView(imageRcv);
            setCount(items.size());
        }
    }

    private void toEditPhotoActivity() {
        ArrayList<String> pathList = new ArrayList<>();
        for (Item item : adapter.getItems()) {
            pathList.add(PathUtils.getPath(this, item.getContentUri()));
        }

        Intent intent = new Intent(this, EditPhotoActivity.class);
        intent.putStringArrayListExtra("PHOTO", pathList);
        startActivity(intent);
    }

    private void setCount(int count) {
        vCount.setText(new StringBuilder()
            .append(count)
            .append(" ")
            .append(selectedString)
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}