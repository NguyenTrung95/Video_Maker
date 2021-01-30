package com.devchie.videomaker.view.nativead;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.devchie.videomaker.R;

public class TemplateView extends FrameLayout {
    static final  boolean $assertionsDisabled = false;
    private static final String MEDIUM_TEMPLATE = "medium_template";
    private static final String SMALL_TEMPLATE = "small_template";
    private LinearLayout background;
    private TextView callToActionParentView;
    private TextView callToActionView;
    private ImageView iconView;
    private MediaView mediaView;
    private UnifiedNativeAd nativeAd;
    private UnifiedNativeAdView nativeAdView;
    private LinearLayout primaryParentView;
    private TextView primaryView;
    private LinearLayout secondaryParentView;
    private TextView secondaryView;
    private NativeTemplateStyle styles;
    private int templateType;
    private LinearLayout tertiaryParentView;
    private TextView tertiaryView;

    public TemplateView(Context context) {
        super(context);
    }

    public TemplateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public TemplateView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet);
    }

    public TemplateView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context, attributeSet);
    }

    public void setStyles(NativeTemplateStyle nativeTemplateStyle) {
        this.styles = nativeTemplateStyle;
        applyStyles();
    }

    public UnifiedNativeAdView getNativeAdView() {
        return this.nativeAdView;
    }

    private void applyStyles() {
        this.styles.getMainBackgroundColor();
        Typeface primaryTextTypeface = this.styles.getPrimaryTextTypeface();
        if (primaryTextTypeface != null) {
            this.primaryView.setTypeface(primaryTextTypeface);
        }
        Typeface secondaryTextTypeface = this.styles.getSecondaryTextTypeface();
        if (secondaryTextTypeface != null) {
            this.secondaryView.setTypeface(secondaryTextTypeface);
        }
        Typeface tertiaryTextTypeface = this.styles.getTertiaryTextTypeface();
        if (tertiaryTextTypeface != null) {
            this.tertiaryView.setTypeface(tertiaryTextTypeface);
        }
        Typeface callToActionTextTypeface = this.styles.getCallToActionTextTypeface();
        if (callToActionTextTypeface != null) {
            this.callToActionView.setTypeface(callToActionTextTypeface);
        }
        int primaryTextTypefaceColor = this.styles.getPrimaryTextTypefaceColor();
        if (primaryTextTypefaceColor > 0) {
            this.primaryView.setTextColor(primaryTextTypefaceColor);
        }
        int secondaryTextTypefaceColor = this.styles.getSecondaryTextTypefaceColor();
        if (secondaryTextTypefaceColor > 0) {
            this.secondaryView.setTextColor(secondaryTextTypefaceColor);
        }
        int tertiaryTextTypefaceColor = this.styles.getTertiaryTextTypefaceColor();
        if (tertiaryTextTypefaceColor > 0) {
            this.tertiaryView.setTextColor(tertiaryTextTypefaceColor);
        }
        int callToActionTypefaceColor = this.styles.getCallToActionTypefaceColor();
        if (callToActionTypefaceColor > 0) {
            this.callToActionView.setTextColor(callToActionTypefaceColor);
        }
        float callToActionTextSize = this.styles.getCallToActionTextSize();
        if (callToActionTextSize > 0.0f) {
            this.callToActionView.setTextSize(callToActionTextSize);
        }
        float primaryTextSize = this.styles.getPrimaryTextSize();
        if (primaryTextSize > 0.0f) {
            this.primaryView.setTextSize(primaryTextSize);
        }
        float secondaryTextSize = this.styles.getSecondaryTextSize();
        if (secondaryTextSize > 0.0f) {
            this.secondaryView.setTextSize(secondaryTextSize);
        }
        float tertiaryTextSize = this.styles.getTertiaryTextSize();
        if (tertiaryTextSize > 0.0f) {
            this.tertiaryView.setTextSize(tertiaryTextSize);
        }
        ColorDrawable callToActionBackgroundColor = this.styles.getCallToActionBackgroundColor();
        if (callToActionBackgroundColor != null) {
            this.callToActionView.setBackground(callToActionBackgroundColor);
        }
        ColorDrawable primaryTextBackgroundColor = this.styles.getPrimaryTextBackgroundColor();
        if (primaryTextBackgroundColor != null) {
            this.primaryView.setBackground(primaryTextBackgroundColor);
        }
        ColorDrawable secondaryTextBackgroundColor = this.styles.getSecondaryTextBackgroundColor();
        if (secondaryTextBackgroundColor != null) {
            this.secondaryView.setBackground(secondaryTextBackgroundColor);
        }
        ColorDrawable tertiaryTextBackgroundColor = this.styles.getTertiaryTextBackgroundColor();
        if (tertiaryTextBackgroundColor != null) {
            this.tertiaryView.setBackground(tertiaryTextBackgroundColor);
        }
        invalidate();
        requestLayout();
    }

    private boolean adHasOnlyStore(UnifiedNativeAd unifiedNativeAd) {
        return !isNullOrEmpty(unifiedNativeAd.getStore()) && isNullOrEmpty(unifiedNativeAd.getAdvertiser());
    }

    private boolean adHasOnlyAdvertiser(UnifiedNativeAd unifiedNativeAd) {
        return !isNullOrEmpty(unifiedNativeAd.getAdvertiser()) && isNullOrEmpty(unifiedNativeAd.getStore());
    }

    private boolean adHasBothStoreAndAdvertiser(UnifiedNativeAd unifiedNativeAd) {
        return !isNullOrEmpty(unifiedNativeAd.getAdvertiser()) && !isNullOrEmpty(unifiedNativeAd.getStore());
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public void setNativeAd(UnifiedNativeAd unifiedNativeAd) {
        this.nativeAd = unifiedNativeAd;
        String store = unifiedNativeAd.getStore();
        String advertiser = unifiedNativeAd.getAdvertiser();
        String headline = unifiedNativeAd.getHeadline();
        String body = unifiedNativeAd.getBody();
        String callToAction = unifiedNativeAd.getCallToAction();
        unifiedNativeAd.getStarRating();
        NativeAd.Image icon = unifiedNativeAd.getIcon();
        this.nativeAdView.setCallToActionView(this.callToActionParentView);
        this.nativeAdView.setHeadlineView(this.primaryParentView);
        this.nativeAdView.setMediaView(this.mediaView);
        if (adHasOnlyStore(unifiedNativeAd)) {
            this.nativeAdView.setStoreView(this.tertiaryView);
            this.tertiaryParentView.setVisibility(View.VISIBLE);
        } else {
            if (adHasOnlyAdvertiser(unifiedNativeAd)) {
                this.nativeAdView.setAdvertiserView(this.tertiaryView);
                this.tertiaryParentView.setVisibility(View.VISIBLE);
                this.secondaryView.setLines(1);
            } else if (adHasBothStoreAndAdvertiser(unifiedNativeAd)) {
                this.nativeAdView.setAdvertiserView(this.tertiaryView);
                this.tertiaryParentView.setVisibility(View.VISIBLE);
                this.secondaryView.setLines(1);
            } else {
                this.tertiaryParentView.setVisibility(View.VISIBLE);
                this.secondaryView.setLines(3);
                store = "";
            }
            store = advertiser;
        }
        this.primaryView.setText(headline);
        this.tertiaryView.setText(body);
        this.secondaryView.setText(store);
        this.callToActionView.setText(callToAction);
        if (icon != null) {
            this.iconView.setVisibility(View.VISIBLE);
            this.iconView.setImageDrawable(icon.getDrawable());
        } else {
            this.iconView.setVisibility(View.GONE);
        }
        this.nativeAdView.setNativeAd(unifiedNativeAd);
    }

    public void destroyNativeAd() {
        this.nativeAd.destroy();
    }

    public String getTemplateTypeName() {
        int i = this.templateType;
        if (i == R.layout.admob_native_medium) {
            return MEDIUM_TEMPLATE;
        }
        return i == R.layout.admob_native_small ? SMALL_TEMPLATE : "";
    }

    private void initView(Context context, AttributeSet attributeSet) {
     int[] TemplateView = {R.attr.gnt_template_type};

        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, TemplateView, 0, 0);
        try {
            this.templateType = obtainStyledAttributes.getResourceId(0, R.layout.admob_native_medium);
            obtainStyledAttributes.recycle();
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.templateType, this);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.nativeAdView = (UnifiedNativeAdView) findViewById(R.id.native_ad_view);
        this.primaryView = (TextView) findViewById(R.id.primary);
        this.secondaryView = (TextView) findViewById(R.id.secondary);
        this.secondaryParentView = (LinearLayout) findViewById(R.id.body);
        this.tertiaryView = (TextView) findViewById(R.id.tertiary);
        this.tertiaryParentView = (LinearLayout) findViewById(R.id.third_line);
        this.callToActionView = (TextView) findViewById(R.id.cta);
        this.iconView = (ImageView) findViewById(R.id.icon);
        this.mediaView = (MediaView) findViewById(R.id.media_view);
        this.primaryParentView = (LinearLayout) findViewById(R.id.headline);
        this.callToActionParentView = (TextView) findViewById(R.id.cta);
    }
}
