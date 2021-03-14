package com.devchie.photoeditor.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devchie.videomaker.R;

import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {
    Context context;
    List<String> fontList;
    FontAdapterClickListener listener;
    int row_selected = -1;

    public interface FontAdapterClickListener {
        void onFontItemSelected(String str);
    }

    public FontAdapter(Context context2, FontAdapterClickListener fontAdapterClickListener, List<String> list) {
        this.context = context2;
        this.listener = fontAdapterClickListener;
        this.fontList = list;
    }

    public FontViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FontViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_font, viewGroup, false),i);
    }

    public void onBindViewHolder(FontViewHolder fontViewHolder, int i) {
        AssetManager assets = this.context.getAssets();
        fontViewHolder.txt_font_demo.setTypeface(Typeface.createFromAsset(assets, "font/" + this.fontList.get(i)));


        if (row_selected == i) {
            fontViewHolder.txt_font_demo.setBackground(context.getDrawable(R.drawable.ic_font_on));
        } else {
            fontViewHolder.txt_font_demo.setBackground(context.getDrawable(R.drawable.ic_font_offf));
        }


    }

    public int getItemCount() {
        return this.fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        FrameLayout font_section;
        TextView txt_font_demo;

        public FontViewHolder(View view,int i) {
            super(view);
            this.txt_font_demo =  view.findViewById(R.id.txt_font_demo);
            this.font_section =  view.findViewById(R.id.font_section);
            font_section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onFontItemSelected(fontList.get(getAdapterPosition()));
                        row_selected = getAdapterPosition();
                        notifyDataSetChanged();

                    }
                }
            });
        }
    }
}
