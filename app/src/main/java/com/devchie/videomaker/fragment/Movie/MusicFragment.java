package com.devchie.videomaker.fragment.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.adaper.MusicAdapter;
import com.devchie.videomaker.model.MusicType;
import java.util.ArrayList;

public class MusicFragment extends Fragment {

    public MusicAdapter adapter;
    private CardView btnMyMusic;

    public ImageView btnMyMusicCheck;

    public MusicFragmentListener listener;
    private RecyclerView recyclerMusic;

    public interface MusicFragmentListener {
        void onMyMusic();

        void onTypeMusic(int i);
    }

    public void setMusicFragmentListener(MusicFragmentListener musicFragmentListener) {
        this.listener = musicFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_movie_music, viewGroup, false);
        this.btnMyMusic = (CardView) inflate.findViewById(R.id.btnMyMusic);
        this.btnMyMusicCheck = (ImageView) inflate.findViewById(R.id.imgUnChecked);
        this.recyclerMusic = (RecyclerView) inflate.findViewById(R.id.recyclerMusic);
        setRecyclerMusic();
        this.btnMyMusic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MusicFragment.this.listener != null) {
                    MusicFragment.this.listener.onMyMusic();
                    MusicFragment.this.adapter.setRowSelected(-1);
                    MusicFragment.this.adapter.notifyDataSetChanged();
                    MusicFragment.this.btnMyMusicCheck.setImageResource(R.drawable.ic_music_check_shape);
                    return;
                }
                Toast.makeText(MusicFragment.this.getActivity(), MusicFragment.this.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        });
        return inflate;
    }

    private void setRecyclerMusic() {
        this.recyclerMusic.setHasFixedSize(true);
        this.recyclerMusic.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MusicType(R.drawable.love, "LOVE"));
        arrayList.add(new MusicType(R.drawable.friendship, "FRIEND"));
        arrayList.add(new MusicType(R.drawable.christmas, "CHRISTMAS"));
        arrayList.add(new MusicType(R.drawable.positive, "POSITIVE"));
        arrayList.add(new MusicType(R.drawable.movie, "MOVIE"));
        arrayList.add(new MusicType(R.drawable.beach, "BEACH"));
        arrayList.add(new MusicType(R.drawable.summer, "SUMMER"));
        arrayList.add(new MusicType(R.drawable.travel, "TRAVEL"));
        arrayList.add(new MusicType(R.drawable.happy, "HAPPY"));
        MusicAdapter musicAdapter = new MusicAdapter(arrayList, getActivity(), new MusicAdapter.MusicAdapterListener() {
            public void onMusicSelected(int i) {
                if (MusicFragment.this.listener != null) {
                    MusicFragment.this.listener.onTypeMusic(i);
                    MusicFragment.this.btnMyMusicCheck.setImageResource(0);
                    return;
                }
                Toast.makeText(MusicFragment.this.getActivity(), MusicFragment.this.getString(R.string.try_again), 0).show();
            }
        });
        this.adapter = musicAdapter;
        this.recyclerMusic.setAdapter(musicAdapter);
    }
}
