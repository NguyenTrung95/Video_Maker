package com.matisse.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.devchie.photoeditor.activity.EditPhotoActivity;
import com.devchie.videomaker.R;
import com.devchie.videomaker.activities.MovieActivity;
import com.devchie.videomaker.ads.AdmobAds;
import com.devchie.videomaker.ads.FacebookAds;
import com.devchie.videomaker.helper.MyConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ProgressDialog progressDialog;
    private ArrayList<String> sendPhotos = new ArrayList<>();
    private List<Item> items;
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
            ProgressDialog progressDialog2 = new ProgressDialog(this);
            this.progressDialog = progressDialog2;
            progressDialog2.setMessage(getString(R.string.com_facebook_loading));
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.show();
            this.sendPhotos.clear();
            for (int i = 0; i < this.items.size(); i++) {
                this.sendPhotos.add(this.items.get(i).uri.toString());
            }
            createMovie();
          /*  Intent resultData = new Intent();
            resultData.putParcelableArrayListExtra(MyConstant.KEY_SELECTED_IMAGES, new ArrayList<>(adapter.getItems()));
            setResult(RESULT_OK, resultData);
            finish();*/
        });
        vBack.setOnClickListener(view -> onBackPressed());
    }

    private void createMovie() {
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

    public void onStop() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            this.progressDialog.dismiss();
        }
        super.onStop();
    }


    private void initImageList() {
        Bundle imageBundle = getIntent().getExtras();
        if (imageBundle != null) {
            items = imageBundle.getParcelableArrayList(MyConstant.KEY_SELECTED_IMAGES);
            adapter = new ImageEditAdapter(items, new ImageEditAdapter.OnItemInteraction() {
                @Override
                public void onItemRemoved(Item item, int position) {
                    setCount(adapter.getItemCount());
                }

                @Override
                public void onEditClick(Item item, int position) {
                    toEditPhotoActivity(position);
                }
            });
            imageRcv.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ImageEditCallback(adapter));
            itemTouchHelper.attachToRecyclerView(imageRcv);
            setCount(items.size());
        }
    }

    private void toEditPhotoActivity(int position) {
        ArrayList<String> pathList = new ArrayList<>();
        for (Item item : adapter.getItems()) {
            pathList.add(PathUtils.getPath(this, item.getContentUri()));
        }

        Intent intent = new Intent(this, EditPhotoActivity.class);
        intent.putStringArrayListExtra("PHOTO", pathList);
        intent.putExtra("POSITION",position);
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