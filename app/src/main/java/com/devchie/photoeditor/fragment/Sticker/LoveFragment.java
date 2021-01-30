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

public class LoveFragment extends Fragment implements StickerAdapter.StickerAdaperListener {
    ArrayList<Sample> heartsList;
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
        this.heartsList = HeartList();
        this.recyclerView.setAdapter(new StickerAdapter(this.heartsList, getActivity(), this));
        return inflate;
    }

    private ArrayList<Sample> HeartList() {
        ArrayList<Sample> arrayList = new ArrayList<>();
        arrayList.add(new Sample(R.drawable.heart1));
        arrayList.add(new Sample(R.drawable.heart2));
        arrayList.add(new Sample(R.drawable.heart3));
        arrayList.add(new Sample(R.drawable.heart5));
        arrayList.add(new Sample(R.drawable.heart6));
        arrayList.add(new Sample(R.drawable.heart7));
        arrayList.add(new Sample(R.drawable.heart8));
        arrayList.add(new Sample(R.drawable.heart9));
        return arrayList;
    }

    public void onStickerSelected(int i) {
        StickerListener stickerListener = this.listener;
        if (stickerListener != null) {
            stickerListener.onStickerClick(i);
        }
    }
}
