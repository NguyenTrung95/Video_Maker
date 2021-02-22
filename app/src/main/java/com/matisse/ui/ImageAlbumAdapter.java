package com.matisse.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devchie.videomaker.R;
import com.matisse.internal.entity.Album;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ImageAlbumAdapter extends RecyclerView.Adapter<ImageAlbumAdapter.ImageAlbumViewHolder> {

    private List<Album> albums = new ArrayList<>();
    private OnItemInteraction onItemInteraction;

    public ImageAlbumAdapter( @NonNull Cursor cursor, OnItemInteraction onItemInteraction) {
        Album album;
        if (cursor.moveToFirst()) {
            do {
                album = Album.valueOf(cursor);
                this.albums.add(album);
            } while (cursor.moveToNext());
        }
        this.onItemInteraction = onItemInteraction;
    }

    @NonNull
    @Override
    public ImageAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_image_album, parent, false);

        return new ImageAlbumViewHolder(root, onItemInteraction);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAlbumViewHolder holder, int position) {
        holder.bind(albums.get(position), holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ImageAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView albumName;

        public ImageAlbumViewHolder(@NonNull View itemView, OnItemInteraction onItemInteraction) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.itemImageAlbumThumb);
            albumName = itemView.findViewById(R.id.itemImageAlbumName);

            itemView.setOnClickListener(view -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && onItemInteraction != null){
                    onItemInteraction.onItemClick(adapterPos);
                }
            });
        }

        public void bind(Album album, Context context) {
            albumName.setText(album.getDisplayName(context));
            thumbnail.setImageURI(album.getCoverUri());
        }
    }

    public interface OnItemInteraction{
        void onItemClick(int position);
    }
}