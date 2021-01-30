package com.devchie.videomaker.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.ads.AdmobAds;
import java.util.ArrayList;
import java.util.List;

public class FacebookAds {

    private static InterstitialAd interstitialAd;
    private static InterstitialAd interstitialAdStart;

    public static AdmobAds.OnAdsCloseListener mOnAdsCloseListener;

    public static void initFullAdStart(final Context context) {
        if (interstitialAdStart == null) {

            InterstitialAd interstitialAd2 = new InterstitialAd(context, context.getString(R.string.facebook_inter_start_full));
            interstitialAdStart = interstitialAd2;
            interstitialAd2.setAdListener(new InterstitialAdListener() {
                public void onAdClicked(Ad ad) {
                }

                public void onInterstitialDisplayed(Ad ad) {
                }

                public void onLoggingImpression(Ad ad) {
                }

                public void onAdLoaded(Ad ad) {
                    Log.d("ADSSSSS", "onAdLoaded");
                }

                public void onInterstitialDismissed(Ad ad) {
                    FacebookAds.loadFullAdStart();
                    if (FacebookAds.mOnAdsCloseListener != null) {
                        FacebookAds.mOnAdsCloseListener.onAdsClose();
                    }
                }

                public void onError(Ad ad, AdError adError) {
                    Log.d("ADSSSSS", "Error " + adError.getErrorMessage());
                }
            });
        }
        loadFullAdStart();
    }


    public static void loadFullAdStart() {
        if (interstitialAdStart != null) {
            Log.d("ADSSSSS", "loadFullAds");
            AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
            interstitialAdStart.loadAd();
        }
    }

    public static boolean showFullAdStart(AdmobAds.OnAdsCloseListener onAdsCloseListener) {
        mOnAdsCloseListener = onAdsCloseListener;
        InterstitialAd interstitialAd2 = interstitialAdStart;
        if (interstitialAd2 == null || !interstitialAd2.isAdLoaded()) {
            return false;
        }
        interstitialAdStart.show();
        return true;
    }

    public static void initFullAds(final Context context) {
        if (interstitialAd == null) {
            InterstitialAd interstitialAd2 = new InterstitialAd(context, context.getString(R.string.facebook_inter_full));
            interstitialAd = interstitialAd2;
            interstitialAd2.setAdListener(new InterstitialAdListener() {
                public void onAdClicked(Ad ad) {
                }

                public void onInterstitialDisplayed(Ad ad) {
                }

                public void onLoggingImpression(Ad ad) {
                }

                public void onAdLoaded(Ad ad) {
                    Log.d("ADSSSSS", "onAdLoaded");
                }

                public void onInterstitialDismissed(Ad ad) {
                    FacebookAds.loadFullAds();
                    if (FacebookAds.mOnAdsCloseListener != null) {
                        FacebookAds.mOnAdsCloseListener.onAdsClose();
                    }
                }

                public void onError(Ad ad, AdError adError) {
                    Log.d("ADSSSSS", "Error " + adError.getErrorMessage());
                }
            });
        }
        loadFullAds();
    }


    public static void loadFullAds() {
        if (interstitialAd != null) {
            Log.d("ADSSSSS", "loadFullAds");
            AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
            interstitialAd.loadAd();
        }
    }

    public static boolean showFullAds(AdmobAds.OnAdsCloseListener onAdsCloseListener) {
        mOnAdsCloseListener = onAdsCloseListener;
        InterstitialAd interstitialAd2 = interstitialAd;
        if (interstitialAd2 == null || !interstitialAd2.isAdLoaded()) {
            return false;
        }
        interstitialAd.show();
        return true;
    }

    public static void destroyAd() {
        InterstitialAd interstitialAd2 = interstitialAd;
        if (interstitialAd2 != null) {
            interstitialAd2.destroy();
        }
        InterstitialAd interstitialAd3 = interstitialAdStart;
        if (interstitialAd3 != null) {
            interstitialAd3.destroy();
        }
    }

    public static void loadBanner(final Activity activity) {
        AdView adView = new AdView((Context) activity, activity.getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);
        ((FrameLayout) activity.findViewById(R.id.fb_banner)).addView(adView);
        adView.setAdListener(new AdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onError(Ad ad, AdError adError) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onAdLoaded(Ad ad) {
                ((View) activity.findViewById(R.id.admob_banner).getParent()).setVisibility(View.GONE);
            }
        });
        AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
        adView.loadAd();
    }

    public static void loadNativeAd(final Activity activity) {
        final NativeAd nativeAd = new NativeAd((Context) activity, activity.getString(R.string.facebook_native_id));
        nativeAd.setAdListener(new NativeAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onMediaDownloaded(Ad ad) {
            }

            public void onError(Ad ad, AdError adError) {
                Log.d("ADSSSSS", "Native Error " + adError.getErrorMessage());
            }

            public void onAdLoaded(Ad ad) {
                ((NativeAdLayout) activity.findViewById(R.id.fb_native_container)).addView(NativeAdView.render((Context) activity, nativeAd, new NativeAdViewAttributes().setButtonColor(activity.getResources().getColor(R.color.button_blue)).setButtonTextColor(-1)));
                AdmobAds.hideAdmobNative(activity);
            }
        });
        AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
        nativeAd.loadAd();
    }

    public static void loadNativeAd_Custom(final Activity activity) {
        final NativeAd nativeAd = new NativeAd((Context) activity, activity.getString(R.string.facebook_native_id));
        nativeAd.setAdListener(new NativeAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onMediaDownloaded(Ad ad) {
            }

            public void onError(Ad ad, AdError adError) {
                Log.d("ADSSSSS", "Native Error " + adError.getErrorMessage());
            }

            public void onAdLoaded(Ad ad) {

                if (nativeAd != null && nativeAd == ad) {
                    nativeAd.unregisterView();
                    NativeAdLayout nativeAdLayout = (NativeAdLayout) activity.findViewById(R.id.fb_native_container);
                    int i = 0;
                    View inflate = LayoutInflater.from(activity).inflate(R.layout.fan_native_medium, nativeAdLayout, false);
                    nativeAdLayout.addView(inflate);
                    LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.ad_choices_container);
                    AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
                    linearLayout.removeAllViews();
                    linearLayout.addView(adOptionsView);
                    AdIconView adIconView = (AdIconView) inflate.findViewById(R.id.icon);
                    TextView textView = (TextView) inflate.findViewById(R.id.primary);
                    MediaView mediaView = (MediaView) inflate.findViewById(R.id.media_view);
                    TextView textView2 = (TextView) inflate.findViewById(R.id.secondary);
                    TextView textView3 = (TextView) inflate.findViewById(R.id.cta);
                    textView.setText(nativeAd.getAdHeadline());
                    ((TextView) inflate.findViewById(R.id.tertiary)).setText(nativeAd.getAdBodyText());
                    if (!nativeAd.hasCallToAction()) {
                        i = 4;
                    }
                    textView3.setVisibility(i);
                    textView3.setText(nativeAd.getAdCallToAction());
                    textView2.setText(nativeAd.getSponsoredTranslation());
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(textView);
                    arrayList.add(textView3);
                    nativeAd.registerViewForInteraction(inflate, mediaView, (MediaView) adIconView, (List<View>) arrayList);
                    AdmobAds.hideAdmobNative(activity);
                }
            }
        });
        AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
        nativeAd.loadAd();
    }

    public static void loadNativeBannerAds_120(final Activity activity) {
        final NativeBannerAd nativeBannerAd = new NativeBannerAd((Context) activity, activity.getString(R.string.facebook_native_banner));
        nativeBannerAd.setAdListener(new NativeAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onMediaDownloaded(Ad ad) {
            }

            public void onError(Ad ad, AdError adError) {
                Log.d("ADSSSSS", "Native Error " + adError.getErrorMessage());
            }

            public void onAdLoaded(Ad ad) {
                ((NativeAdLayout) activity.findViewById(R.id.fb_native_container)).addView(NativeBannerAdView.render(activity, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120));
                AdmobAds.hideAdmobNative(activity);
            }
        });
        AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
        nativeBannerAd.loadAd();
    }
}
