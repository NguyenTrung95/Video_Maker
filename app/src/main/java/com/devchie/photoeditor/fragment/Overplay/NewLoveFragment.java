package com.devchie.photoeditor.fragment.Overplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devchie.photoeditor.fragment.Sticker.StickerAdapter;
import com.devchie.videomaker.R;
import com.devchie.videomaker.listener.OverlaysListener;
import com.devchie.videomaker.model.Sample;

import java.util.ArrayList;

public class NewLoveFragment extends Fragment implements StickerAdapter.StickerAdaperListener {
    ArrayList<Sample> chistmasArrays;
    OverlaysListener listener;
    RecyclerView recyclerView;

    public void setOverlaysListener(OverlaysListener stickerListener) {
        this.listener = stickerListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sticker_recyclerview, viewGroup, false);
        RecyclerView findViewById = inflate.findViewById(R.id.recyclerSticker);
        this.recyclerView = findViewById;
        findViewById.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        this.chistmasArrays = ChistmasList();
        this.recyclerView.setAdapter(new StickerAdapter(this.chistmasArrays, getActivity(), this));
        return inflate;
    }

    public ArrayList<Sample> ChistmasList() {
        ArrayList<Sample> arrayList = new ArrayList<>();
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        return arrayList;
    }

    public void onStickerSelected(int i) {
        OverlaysListener stickerListener = this.listener;
        if (stickerListener != null) {
            stickerListener.onOverlaysClick(i);
        }
    }
}
