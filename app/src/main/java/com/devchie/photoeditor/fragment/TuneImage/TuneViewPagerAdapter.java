package com.devchie.photoeditor.fragment.TuneImage;

import android.content.Context;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.devchie.videomaker.listener.TuneImageFragmentListener;

public class TuneViewPagerAdapter extends FragmentPagerAdapter implements BrightnessFragment.BrightnessFragmentListener, ContrastFragment.ConstrastFragmentListener, HueFragment.HueFragmentListener, SaturationFragment.SaturationFragmentListener {
    private Fragment[] chilFragment;
    private Context mContext;
    TuneImageFragmentListener mlistener;

    public CharSequence getPageTitle(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "" : "Saturation" : "Hue" : "Constrast" : "Brightness";
    }

    public TuneViewPagerAdapter(FragmentManager fragmentManager, Context context, TuneImageFragmentListener tuneImageFragmentListener) {
        super(fragmentManager);
        BrightnessFragment brightnessFragment = new BrightnessFragment();
        brightnessFragment.setBrightnessListener(this);
        ContrastFragment contrastFragment = new ContrastFragment();
        contrastFragment.setConstrastListener(this);
        HueFragment hueFragment = new HueFragment();
        hueFragment.setHueListener(this);
        SaturationFragment saturationFragment = new SaturationFragment();
        saturationFragment.setSaturationListener(this);
        this.chilFragment = new Fragment[]{brightnessFragment, contrastFragment, hueFragment, saturationFragment};
        this.mContext = context;
        this.mlistener = tuneImageFragmentListener;
    }

    public Fragment getItem(int i) {
        return this.chilFragment[i];
    }

    public int getCount() {
        return this.chilFragment.length;
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        TuneViewPagerAdapter.super.setPrimaryItem(viewGroup, i, obj);
    }

    public void onBrightnessChoose(int i) {
        this.mlistener.onBrightnessChanged(i);
    }

    public void onConstrastChoose(int i) {
        this.mlistener.onConstrantChanged(i);
    }

    public void onHueChoose(int i) {
        this.mlistener.onHueChanged(i);
    }

    public void onSaturationChoose(int i) {
        this.mlistener.onSaturationChanged(i);
    }
}
