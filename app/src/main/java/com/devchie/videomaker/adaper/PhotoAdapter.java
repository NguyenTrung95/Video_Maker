package com.devchie.videomaker.adaper;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.devchie.videomaker.R;
import com.devchie.videomaker.listener.ItemClickListener;
import com.devchie.videomaker.model.Photo;
import com.devchie.videomaker.view.SquareImageView;
import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoAdapterViewHolder> implements DraggableItemAdapter<PhotoAdapter.PhotoAdapterViewHolder> {
    ItemClickListener listener;
    private Context mContext;
    private ArrayList<Photo> mPhotos = new ArrayList<>();

    public boolean onCheckCanDrop(int i, int i2) {
        return true;
    }

    public boolean onCheckCanStartDrag(PhotoAdapterViewHolder photoAdapterViewHolder, int i, int i2, int i3) {
        return true;
    }

    public ItemDraggableRange onGetItemDraggableRange(PhotoAdapterViewHolder photoAdapterViewHolder, int i) {
        return null;
    }

    public void onItemDragFinished(int i, int i2, boolean z) {
    }

    public void onItemDragStarted(int i) {
    }

    public PhotoAdapter(ArrayList<Photo> arrayList, Context context, ItemClickListener itemClickListener) {
        this.mPhotos = arrayList;
        this.mContext = context;
        this.listener = itemClickListener;
    }

    public long getItemId(int i) {
        return this.mPhotos.get(i).id;
    }

    public PhotoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
        final PhotoAdapterViewHolder photoAdapterViewHolder = new PhotoAdapterViewHolder(inflate);
        inflate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotoAdapter.this.listener.onItemClick(view, photoAdapterViewHolder.getLayoutPosition());
            }
        });
        inflate.findViewById(R.id.item_photo_close_container).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotoAdapter.this.listener.onItemDeleteClick(view, photoAdapterViewHolder.getLayoutPosition());
            }
        });
        return photoAdapterViewHolder;
    }

    public void onBindViewHolder(PhotoAdapterViewHolder photoAdapterViewHolder, int i) {
        Uri fromFile = Uri.fromFile(new File(this.mPhotos.get(i).paths));
        Glide.with(this.mContext).load(fromFile).apply((BaseRequestOptions<?>) new RequestOptions()).thumbnail(0.1f).into((ImageView) photoAdapterViewHolder.ivPhoto);
    }

    public int getItemCount() {
        return this.mPhotos.size();
    }

    public void onMoveItem(int i, int i2) {
        this.mPhotos.add(i2, this.mPhotos.remove(i));
    }

    public class PhotoAdapterViewHolder extends AbstractDraggableItemViewHolder {

        public SquareImageView ivPhoto;

        public PhotoAdapterViewHolder(View view) {
            super(view);
            this.ivPhoto = (SquareImageView) view.findViewById(R.id.imgViewPhoto);
        }
    }
}
