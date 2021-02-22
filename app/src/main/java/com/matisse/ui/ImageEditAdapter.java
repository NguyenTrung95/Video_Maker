package com.matisse.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devchie.videomaker.R;
import com.matisse.internal.entity.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageEditAdapter extends RecyclerView.Adapter<ImageEditAdapter.ImageEditViewHolder> {

    private List<Item> items = new ArrayList<>();
    private OnItemInteraction onItemInteraction;

    public ImageEditAdapter(List<Item> items, OnItemInteraction onItemInteraction) {
        if (items != null) {
            this.items.addAll(items);
        }
        this.onItemInteraction = onItemInteraction;
    }

    @NonNull
    @Override
    public ImageEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_image_editable, parent, false);
        ImageEditViewHolder holder = new ImageEditViewHolder(root);
        holder.remove.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Item deletedItem = items.get(pos);
                items.remove(pos);
                notifyItemRemoved(pos);
                if (onItemInteraction != null) {
                    onItemInteraction.onItemRemoved(deletedItem, pos);
                }
            }
        });

        holder.edit.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION && onItemInteraction != null) {
                onItemInteraction.onEditClick(items.get(pos), pos);
            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageEditViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public void swap(int fromPos, int toPos) {
        try {
            if (fromPos < toPos) {
                for (int i = fromPos; i < toPos; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = toPos; i < fromPos; i++) {
                    Collections.swap(items, i, i + 1);
                }
            }
            notifyItemMoved(fromPos, toPos);
        } catch (Exception ignored) {
        }
    }

    static class ImageEditViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        View remove;
        View edit;

        public ImageEditViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImageEditableImage);
            edit = itemView.findViewById(R.id.itemImageEditableEdit);
            remove = itemView.findViewById(R.id.itemImageEditableRemove);
        }

        public void bind(Item item) {
            image.setImageURI(item.getContentUri());
        }

    }

    public interface OnItemInteraction {
        void onItemRemoved(Item item, int position);

        void onEditClick(Item item, int position);
    }
}