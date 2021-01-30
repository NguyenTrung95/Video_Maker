package com.devchie.videomaker.fragment.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.adaper.FilterAdapter;
import com.devchie.videomaker.model.FilterItem;
import com.devchie.videomaker.model.FilterType;
import java.util.ArrayList;

public class FilterFragment extends Fragment {
    FilterAdapter adapter;
    FilterFragmentListener mfilterFragmentListener;
    RecyclerView recyclerFilter;

    public interface FilterFragmentListener {
        void onFilter(FilterItem filterItem);
    }

    public void setFilterFragmentListener(FilterFragmentListener filterFragmentListener) {
        this.mfilterFragmentListener = filterFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_movie_filter, viewGroup, false);
        this.recyclerFilter = (RecyclerView) inflate.findViewById(R.id.recyclerFilter);
        setRecyclerFilter();
        return inflate;
    }

    private void setRecyclerFilter() {
        this.recyclerFilter.setHasFixedSize(true);
        this.recyclerFilter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterItem(R.drawable.filter, "Normal", FilterType.NONE));
        arrayList.add(new FilterItem(R.drawable.filter_gray, "Black White", FilterType.GRAY));
        arrayList.add(new FilterItem(R.drawable.filter_snow, "Snow", FilterType.SNOW));
        arrayList.add(new FilterItem(R.drawable.filter_l1, "Walden", FilterType.LUT1));
        arrayList.add(new FilterItem(R.drawable.filter_l2, "Lut 2", FilterType.LUT2));
        arrayList.add(new FilterItem(R.drawable.filter_l3, "Lut 3", FilterType.LUT3));
        arrayList.add(new FilterItem(R.drawable.filter_l4, "Lut 4", FilterType.LUT4));
        arrayList.add(new FilterItem(R.drawable.filter_l5, "Lut 5", FilterType.LUT5));
        arrayList.add(new FilterItem(R.drawable.cameo, "Cameo", FilterType.CAMEO));
        FilterAdapter filterAdapter = new FilterAdapter(arrayList, getActivity(), new FilterAdapter.FilterAdapterListener() {
            public void onFilterSelected(FilterItem filterItem) {
                if (FilterFragment.this.mfilterFragmentListener != null) {
                    FilterFragment.this.mfilterFragmentListener.onFilter(filterItem);
                } else {
                    Toast.makeText(FilterFragment.this.getActivity(), FilterFragment.this.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.adapter = filterAdapter;
        this.recyclerFilter.setAdapter(filterAdapter);
    }
}
