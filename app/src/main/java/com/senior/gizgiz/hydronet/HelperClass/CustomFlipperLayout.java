package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 017 17/1/2018.
 */

//public class CustomFlipperLayout extends RelativeLayout {
public class CustomFlipperLayout extends RelativeLayout {
    View rootView,firstPage,secondPage;
    ViewFlipper flipper;

    String firstPageLayout,secondPageLayout;
    private ImageButton flipButton;

    public CustomFlipperLayout(Context context) { super(context); }
    public CustomFlipperLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomFlipperLayout,
                0, 0);
        try {
            firstPageLayout = a.getString(R.styleable.CustomFlipperLayout_first_page_layout);
            secondPageLayout = a.getString(R.styleable.CustomFlipperLayout_second_page_layout);
        } finally {
            a.recycle();
        }
        init(context,firstPageLayout,secondPageLayout);
    }

    public CustomFlipperLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomFlipperLayout,
                0, 0);
        try {
            firstPageLayout = a.getString(R.styleable.CustomFlipperLayout_first_page_layout);
            secondPageLayout = a.getString(R.styleable.CustomFlipperLayout_second_page_layout);
        } finally {
            a.recycle();
        }
        init(context,firstPageLayout,secondPageLayout);
    }
    public void setFirstPageLayout(String firstPageLayout) { this.firstPageLayout = firstPageLayout; }
    public void setSecondPageLayout(String secondPageLayout) { this.secondPageLayout = secondPageLayout; }

    public ViewFlipper getFlipper() { return flipper; }

    public View getFirstPage() {
        return firstPage;
    }

    public View getSecondPage() {
        return secondPage;
    }

    public void setViewFirstPage() {
        flipper.setDisplayedChild(0);
        flipper.setInAnimation(null);
        flipper.setOutAnimation(null);
    }
    public void setViewSecondPage() {
        flipper.setDisplayedChild(1);
        flipper.setInAnimation(null);
        flipper.setOutAnimation(null);
    }

    private void init (final Context context, String firstPageLayout, String secondPageLayout) {
        rootView = inflate(context, R.layout.flipper, this);
        flipper = rootView.findViewById(R.id.flipper);

        int firstPageId = ResourceManager.getLayoutID(getContext(),firstPageLayout);
        int secondPageId = ResourceManager.getLayoutID(getContext(),secondPageLayout);

        ViewStub firstPageStub = rootView.findViewById(R.id.first_page_stub);
        firstPageStub.setLayoutResource(firstPageId);
        firstPage = firstPageStub.inflate();

        ViewStub secondPageStub = rootView.findViewById(R.id.second_page_stub);
        secondPageStub.setLayoutResource(secondPageId);
        secondPage = secondPageStub.inflate();

        handleFlipButton();

        invalidate();
        requestLayout();
    }

    public void handleFlipButton() {
        flipButton = rootView.findViewById(R.id.expand_activities_button);
        flipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_up));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_down));
                flipper.setDisplayedChild(1);
            }
        });
    }

    public void resetFlipper() {
        flipper.removeAllViews();
//        flipper.re
//        flipper.addView();
    }
}