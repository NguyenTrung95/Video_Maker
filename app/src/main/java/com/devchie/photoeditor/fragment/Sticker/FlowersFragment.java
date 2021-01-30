package com.devchie.photoeditor.fragment.Sticker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.listener.StickerListener;
import com.devchie.videomaker.model.Sample;
import com.devchie.videomaker.R;

import java.util.ArrayList;

public class FlowersFragment extends Fragment implements StickerAdapter.StickerAdaperListener {
    ArrayList<Sample> flowersList;
    StickerListener listener;
    RecyclerView recyclerView;

    public void setStickerListener(StickerListener stickerListener) {
        this.listener = stickerListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sticker_recyclerview, viewGroup, false);
        RecyclerView findViewById = inflate.findViewById(R.id.recyclerSticker);
        this.recyclerView = findViewById;
        findViewById.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        this.flowersList = FlowerList();
        this.recyclerView.setAdapter(new StickerAdapter(this.flowersList, getActivity(), this));
        return inflate;
    }

    private ArrayList<Sample> FlowerList() {
        ArrayList<Sample> arrayList = new ArrayList<>();
        arrayList.add(new Sample(R.drawable.deco1));
        arrayList.add(new Sample(R.drawable.deco2));
        arrayList.add(new Sample(R.drawable.deco3));
        arrayList.add(new Sample(R.drawable.deco4));
        arrayList.add(new Sample(R.drawable.deco5));
        arrayList.add(new Sample(R.drawable.deco7));
        arrayList.add(new Sample(R.drawable.deco8));
        arrayList.add(new Sample(R.drawable.deco9));
        arrayList.add(new Sample(R.drawable.deco10));
        return arrayList;
    }

    public void onStickerSelected(int i) {
        this.listener.onStickerClick(i);
    }
}
