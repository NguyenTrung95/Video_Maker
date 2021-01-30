package com.devchie.photoeditor.fragment.Sticker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.listener.StickerListener;
import com.devchie.videomaker.model.Sample;

import java.util.ArrayList;

public class FireWorkFragment extends Fragment implements StickerAdapter.StickerAdaperListener {
    ArrayList<Sample> fireWorkList;
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
        this.fireWorkList = FireWorks();
        this.recyclerView.setAdapter(new StickerAdapter(this.fireWorkList, getActivity(), this));
        return inflate;
    }

    public ArrayList<Sample> FireWorks() {
        ArrayList<Sample> arrayList = new ArrayList<>();
        arrayList.add(new Sample(R.drawable.firework1));
        arrayList.add(new Sample(R.drawable.firework2));
        arrayList.add(new Sample(R.drawable.firework3));
        arrayList.add(new Sample(R.drawable.firework4));
        arrayList.add(new Sample(R.drawable.firework5));
        arrayList.add(new Sample(R.drawable.firework6));
        arrayList.add(new Sample(R.drawable.firework7));
        arrayList.add(new Sample(R.drawable.firework8));
        arrayList.add(new Sample(R.drawable.firework9));
        arrayList.add(new Sample(R.drawable.firework10));
        arrayList.add(new Sample(R.drawable.firework11));
        return arrayList;
    }

    public void onStickerSelected(int i) {
        this.listener.onStickerClick(i);
    }
}
