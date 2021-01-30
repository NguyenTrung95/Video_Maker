package com.devchie.videomaker.fragment.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hw.photomovie.PhotoMovieFactory;
import com.devchie.videomaker.R;
import com.devchie.videomaker.adaper.TransferAdapter;
import com.devchie.videomaker.model.TransferItem;
import java.util.ArrayList;

public class TransferFragment extends Fragment {
    TransferAdapter adapter;
    TransferFragmentListener mListener;
    RecyclerView recyclerTransfer;

    public interface TransferFragmentListener {
        void onTransfer(TransferItem transferItem);
    }

    public void setTransferFragmentListener(TransferFragmentListener transferFragmentListener) {
        this.mListener = transferFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_movie_transfer, viewGroup, false);
        this.recyclerTransfer = (RecyclerView) inflate.findViewById(R.id.recyclerTransfer);
        setRecyclerTransfer();
        return inflate;
    }

    private void setRecyclerTransfer() {
        this.recyclerTransfer.setHasFixedSize(true);
        this.recyclerTransfer.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TransferItem(R.drawable.random_transfer, getString(R.string.str_random), PhotoMovieFactory.PhotoMovieType.RANDOM));
        arrayList.add(new TransferItem(R.drawable.gradient_transfer, getString(R.string.str_gradient), PhotoMovieFactory.PhotoMovieType.GRADIENT));
        arrayList.add(new TransferItem(R.drawable.window_transfer, getString(R.string.str_window), PhotoMovieFactory.PhotoMovieType.WINDOW));
        arrayList.add(new TransferItem(R.drawable.tranlastion_transfer, getString(R.string.str_tranlation), PhotoMovieFactory.PhotoMovieType.SCALE_TRANS));
        arrayList.add(new TransferItem(R.drawable.leftright_transfer, getString(R.string.str_leftright), PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS));
        arrayList.add(new TransferItem(R.drawable.up_down_transfer, getString(R.string.str_updown), PhotoMovieFactory.PhotoMovieType.VERTICAL_TRANS));
        arrayList.add(new TransferItem(R.drawable.thaw_transfer, getString(R.string.str_thaw), PhotoMovieFactory.PhotoMovieType.THAW));
        arrayList.add(new TransferItem(R.drawable.scale_transfer, getString(R.string.str_scale), PhotoMovieFactory.PhotoMovieType.SCALE));
        TransferAdapter transferAdapter = new TransferAdapter(arrayList, getActivity(), new TransferAdapter.TransferAdapterListener() {
            public void onTransferSelected(TransferItem transferItem) {
                if (TransferFragment.this.mListener != null) {
                    TransferFragment.this.mListener.onTransfer(transferItem);
                } else {
                    Toast.makeText(TransferFragment.this.getActivity(), TransferFragment.this.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.adapter = transferAdapter;
        this.recyclerTransfer.setAdapter(transferAdapter);
    }
}
