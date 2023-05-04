// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.ads.nativetemplates;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.nativead.AdChoicesView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

/**
 * Base class for a template view. *
 */
public class TemplateView extends FrameLayout {

  private NativeTemplateStyle styles;
  private NativeAd nativeAd;
  private NativeAdView nativeAdView;
  private TextView primaryView;
  private TextView tertiaryView;
  private ImageView iconView;
  private MediaView mediaView;
  private Button callToActionView;
  private AdChoicesView adChoicesView;
  private ConstraintLayout background;


  public TemplateView(Context context) {
    super(context);
  }

  public TemplateView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initView(context, attrs);
  }

  public TemplateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context, attrs);
  }

  public TemplateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView(context, attrs);
  }

  public void setStyles(NativeTemplateStyle styles) {
    this.styles = styles;
    this.applyStyles();
  }

  private void applyStyles() {
    Drawable mainBackground = styles.getMainBackgroundColor();
    if (mainBackground != null) {
      background.setBackground(mainBackground);
      if (primaryView != null) primaryView.setBackground(mainBackground);
      if (tertiaryView != null) tertiaryView.setBackground(mainBackground);
    }

    Typeface primary = styles.getPrimaryTextTypeface();
    if (primary != null && primaryView != null) primaryView.setTypeface(primary);

    Typeface tertiary = styles.getTertiaryTextTypeface();
    if (tertiary != null && tertiaryView != null) tertiaryView.setTypeface(tertiary);


    Typeface ctaTypeface = styles.getCallToActionTextTypeface();
    if (ctaTypeface != null && callToActionView != null)
      callToActionView.setTypeface(ctaTypeface);

    if (styles.getPrimaryTextTypefaceColor() != null && primaryView != null)
      primaryView.setTextColor(styles.getPrimaryTextTypefaceColor());

    if (styles.getTertiaryTextTypefaceColor() != null && tertiaryView != null)
      tertiaryView.setTextColor(styles.getTertiaryTextTypefaceColor());

    if (styles.getCallToActionTypefaceColor() != null && callToActionView != null)
      callToActionView.setTextColor(styles.getCallToActionTypefaceColor());

    float ctaTextSize = styles.getCallToActionTextSize();
    if (ctaTextSize > 0 && callToActionView != null)
      callToActionView.setTextSize(ctaTextSize);

    float primaryTextSize = styles.getPrimaryTextSize();
    if (primaryTextSize > 0 && primaryView != null)
      primaryView.setTextSize(primaryTextSize);

    float tertiaryTextSize = styles.getTertiaryTextSize();
    if (tertiaryTextSize > 0 && tertiaryView != null)
      tertiaryView.setTextSize(tertiaryTextSize);

    Drawable ctaBackground = styles.getCallToActionBackgroundColor();
    if (ctaBackground != null && callToActionView != null)
      callToActionView.setBackground(ctaBackground);

    Drawable primaryBackground = styles.getPrimaryTextBackgroundColor();
    if (primaryBackground != null && primaryView != null)
      primaryView.setBackground(primaryBackground);

    Drawable tertiaryBackground = styles.getTertiaryTextBackgroundColor();
    if (tertiaryBackground != null && tertiaryView != null)
      tertiaryView.setBackground(tertiaryBackground);

    invalidate();
    requestLayout();
  }

  public NativeAd getNativeAd(){
    return this.nativeAd;
  }
  public void setNativeAd(NativeAd nativeAd) {
    this.nativeAd = nativeAd;
    String headline = nativeAd.getHeadline();
    String body = nativeAd.getBody();
    String cta = nativeAd.getCallToAction();
    NativeAd.Image icon = nativeAd.getIcon();

    if (!TextUtils.isEmpty(headline) && headline.length() > 25) {
      String sub = headline.substring(0, 25);
      int endSpace = sub.lastIndexOf(" ");
      if (endSpace > 22) {
        headline = sub.substring(0, endSpace);
        sub = headline.substring(0, headline.lastIndexOf(" "));
        headline = sub + " ...";
      } else if (endSpace > -1) {
        headline = sub.substring(0, endSpace);
        headline = headline + " ...";
      } else {
        headline = sub.substring(0, 23) + "...";
      }
    }
    if (!TextUtils.isEmpty(body) && body.length() > 90) {
      String sub = body.substring(0, 90);
      int endSpace = sub.lastIndexOf(" ");
      if (endSpace > 87) {
        body = sub.substring(0, endSpace);
        sub = body.substring(0, body.lastIndexOf(" "));
        body = sub + " ...";
      } else if (endSpace > -1) {
        body = sub.substring(0, endSpace);
        body = body + " ...";
      } else {
        body = sub.substring(0, 88) + "...";
      }
    }

    nativeAdView.setCallToActionView(callToActionView);
    nativeAdView.setAdChoicesView(adChoicesView);
    nativeAdView.setHeadlineView(primaryView);
    nativeAdView.setMediaView(mediaView);
    primaryView.setText(headline);
    callToActionView.setText(cta);
    if (icon != null) {
      iconView.setVisibility(VISIBLE);
      iconView.setImageDrawable(icon.getDrawable());
    } else {
      iconView.setVisibility(GONE);
    }
    if (tertiaryView != null) {
      tertiaryView.setText(body);
      nativeAdView.setBodyView(tertiaryView);
    }
    nativeAdView.setNativeAd(nativeAd);
    shimmer.stopShimmer();
    shimmer.setVisibility(GONE);
    background.setVisibility(VISIBLE);
  }

  /**
   * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
   * method does not destroy the template view.
   * <a href="https://developers.google.com/admob/android/native-unified#destroy_ad">...</a>
   */
  public void destroyNativeAd() {
    if (nativeAd != null) {
      nativeAd.destroy();
      nativeAd = null;
    }
  }

  private void initView(Context context, AttributeSet attributeSet) {

    TypedArray attributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0);
    int templateType;
    try {
      templateType = attributes.getResourceId(R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_3_template_view);
    } finally {
      attributes.recycle();
    }
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(templateType, this);
  }

  private ShimmerFrameLayout shimmer;

  public void preLoadView() {
    background.setVisibility(GONE);
    shimmer.setVisibility(VISIBLE);
    shimmer.startShimmer();
  }

  @Override
  public void onFinishInflate() {
    super.onFinishInflate();
    nativeAdView = findViewById(R.id.native_ad_view);
    primaryView = findViewById(R.id.primary);
    tertiaryView = findViewById(R.id.body);

    callToActionView = findViewById(R.id.cta);
    iconView = findViewById(R.id.icon);
    mediaView = findViewById(R.id.media_view);
    background = findViewById(R.id.background);
    adChoicesView = findViewById(R.id.ad_choice_view);
    shimmer = findViewById(R.id.shimmer_view);
    preLoadView();
  }
}
