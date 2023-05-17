package com.thanhlv.mylibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.thanhlv.mylibrary.R;

public class BannerAdView extends RelativeLayout {
    public BannerAdView(Context context) {
        super(context);
    }

    public BannerAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BannerAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public BannerAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.BannerAdView, 0, 0);
        int templateType;
        try {
            templateType = attributes.getResourceId(R.styleable.BannerAdView_banner_type, R.layout.banner_full_width_layout);
        } finally {
            attributes.recycle();
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(templateType, this);
    }

    public void toggleLoadingView(boolean show) {
        if (loadingView != null) {
            if (show) {
                loadingView.setVisibility(VISIBLE);
                loadingView.startShimmer();
            } else {
                loadingView.stopShimmer();
                loadingView.setVisibility(GONE);
            }
        }
    }


    ShimmerFrameLayout loadingView;
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        loadingView = findViewById(R.id.loading_view);
        toggleLoadingView(true);
    }
}
