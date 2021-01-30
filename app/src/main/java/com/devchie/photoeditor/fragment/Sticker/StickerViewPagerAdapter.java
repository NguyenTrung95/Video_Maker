package com.devchie.photoeditor.fragment.Sticker;

import android.content.Context;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.devchie.videomaker.listener.StickerListener;

public class StickerViewPagerAdapter extends FragmentPagerAdapter implements StickerListener {
    private Fragment[] chilFragment;
    private Context mContext;
    StickerViewPagerAdapterListener mlistener;

    public interface StickerViewPagerAdapterListener {
        void onSticker(int i);
    }

    public CharSequence getPageTitle(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "" : "Firework" : "Flowers" : "Chistmas" : "Light" : "Heart";
    }

    public StickerViewPagerAdapter(FragmentManager fragmentManager, Context context, StickerViewPagerAdapterListener stickerViewPagerAdapterListener) {
        super(fragmentManager);
        ChistmasFragment chistmasFragment = new ChistmasFragment();
        chistmasFragment.setStickerListener(this);
        LightFragment lightFragment = new LightFragment();
        lightFragment.setStickerListener(this);
        LoveFragment loveFragment = new LoveFragment();
        loveFragment.setStickerListener(this);
        FlowersFragment flowersFragment = new FlowersFragment();
        flowersFragment.setStickerListener(this);
        FireWorkFragment fireWorkFragment = new FireWorkFragment();
        fireWorkFragment.setStickerListener(this);
        this.chilFragment = new Fragment[]{loveFragment, lightFragment, chistmasFragment, flowersFragment, fireWorkFragment};
        this.mContext = context;
        this.mlistener = stickerViewPagerAdapterListener;
    }

    public StickerViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        return this.chilFragment[i];
    }

    public int getCount() {
        return this.chilFragment.length;
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        StickerViewPagerAdapter.super.setPrimaryItem(viewGroup, i, obj);
    }

    public void onStickerClick(int i) {
        this.mlistener.onSticker(i);
    }
}
