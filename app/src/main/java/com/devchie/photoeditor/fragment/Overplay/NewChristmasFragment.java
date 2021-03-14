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

public class NewChristmasFragment extends Fragment implements StickerAdapter.StickerAdaperListener {
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

    /*public ArrayList<Sample> ChistmasList() {
        ArrayList<Sample> arrayList = new ArrayList<>();
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        arrayList.add(new Sample(R.drawable.item_overlays));
        return arrayList;
    }*/
    public ArrayList<Sample> ChistmasList() {
        int[] christmasList = {R.drawable.christmas0, R.drawable.christmas1, R.drawable.christmas2, R.drawable.christmas3, R.drawable.christmas4, R.drawable.christmas5, R.drawable.christmas6, R.drawable.christmas7, R.drawable.christmas8, R.drawable.christmas9, R.drawable.christmas10, R.drawable.christmas11, R.drawable.christmas12, R.drawable.christmas13, R.drawable.christmas14, R.drawable.christmas15, R.drawable.christmas16, R.drawable.christmas17, R.drawable.christmas18, R.drawable.christmas19, R.drawable.christmas20, R.drawable.christmas21, R.drawable.christmas22, R.drawable.christmas23, R.drawable.christmas24, R.drawable.christmas25, R.drawable.christmas26, R.drawable.christmas27, R.drawable.christmas28, R.drawable.christmas29, R.drawable.christmas30};
        ArrayList<Sample> arrayList = new ArrayList<>();

        for (int i : christmasList) {
            arrayList.add(new Sample(i));
        }

        return arrayList;
    }


    /*@Override
    public void onOverlaysSelected(int i) {
        OverlaysListener overlaysListener = this.listener;
        if (overlaysListener != null) {
            overlaysListener.onOverlaysClick(i);
        }
    }*/

    @Override
    public void onStickerSelected(int i) {
        OverlaysListener stickerListener = this.listener;
        if (stickerListener != null) {
            stickerListener.onOverlaysClick(i);
        }
    }
}
