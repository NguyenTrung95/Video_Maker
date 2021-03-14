package com.devchie.photoeditor.fragment.Overplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devchie.videomaker.R;
import com.devchie.videomaker.model.Sample;
import com.devchie.videomaker.view.SquareImageView;

import java.util.ArrayList;

public class OverlaysAdapter extends RecyclerView.Adapter<OverlaysAdapter.ViewHolderSticker> {
    OverlaysAdaperListener listener;
    Context mContext;
    ArrayList<Sample> stickerArrayList;

    public interface OverlaysAdaperListener {
        void onOverlaysSelected(int i);
    }

    public OverlaysAdapter(ArrayList<Sample> arrayList, Context context, OverlaysAdaperListener stickerAdaperListener) {
        this.stickerArrayList = arrayList;
        this.mContext = context;
        this.listener = stickerAdaperListener;
    }

    public ViewHolderSticker onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolderSticker(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolderSticker viewHolderSticker, int i) {
        Glide.with(this.mContext).load(Integer.valueOf(this.stickerArrayList.get(i).getImgSample())).thumbnail(0.1f).into(viewHolderSticker.imgSticker);
    }

    public int getItemCount() {
        return this.stickerArrayList.size();
    }

    public class ViewHolderSticker extends RecyclerView.ViewHolder {
        SquareImageView imgSticker;

        public ViewHolderSticker(View view) {
            super(view);
            this.imgSticker = view.findViewById(R.id.imgSticker);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OverlaysAdapter.this.listener.onOverlaysSelected(OverlaysAdapter.this.stickerArrayList.get(ViewHolderSticker.this.getAdapterPosition()).getImgSample());
                }
            });
        }
    }
}
