package com.devchie.photoeditor.fragment.Sticker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.devchie.videomaker.R;

public class StickerFragment extends Fragment {
    StickerFragmentListener listener;
    TabLayout tabLayoutSticker;
    ViewPager viewPagerSticker;

    public interface StickerFragmentListener {
        void onStickerFragmentClick(Bitmap bitmap);
    }

    public void setStickerFragmentListener(StickerFragmentListener stickerFragmentListener) {
        this.listener = stickerFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_sticker, viewGroup, false);
        this.tabLayoutSticker = inflate.findViewById(R.id.tablayoutSticker);
        ViewPager findViewById = inflate.findViewById(R.id.viewpagerSticker);
        this.viewPagerSticker = findViewById;
        findViewById.setAdapter(new StickerViewPagerAdapter(getChildFragmentManager(), getActivity(), new StickerViewPagerAdapter.StickerViewPagerAdapterListener() {
            public void onSticker(int i) {
                if (StickerFragment.this.listener != null) {
                    StickerFragment.this.listener.onStickerFragmentClick(BitmapFactory.decodeResource(StickerFragment.this.getResources(), i));
                }
            }
        }));
        this.tabLayoutSticker.setupWithViewPager(this.viewPagerSticker);
        changeTabsFont();
        return inflate;
    }

    private void changeTabsFont() {
        ViewGroup viewGroup = (ViewGroup) this.tabLayoutSticker.getChildAt(0);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(i);
            int childCount2 = viewGroup2.getChildCount();
            for (int i2 = 0; i2 < childCount2; i2++) {
                View childAt = viewGroup2.getChildAt(i2);
                if (childAt instanceof TextView) {
                    ((TextView) childAt).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), new StringBuilder("font/slabo.ttf").toString()));
                }
            }
        }
    }
}
