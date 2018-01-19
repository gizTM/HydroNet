package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class TwoPageFlipperLayout extends RelativeLayout {
    View rootView,firstPage,secondPage;
    ViewFlipper flipper;
    ImageButton flipButton;

    String firstPageLayout,secondPageLayout;

    public TwoPageFlipperLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.TwoPageFlipperLayout,
                0, 0);
        try {
            firstPageLayout = a.getString(R.styleable.TwoPageFlipperLayout_first_page_layout);
            secondPageLayout = a.getString(R.styleable.TwoPageFlipperLayout_second_page_layout);
        } finally {
            a.recycle();
        }
        init(context,firstPageLayout,secondPageLayout);
    }

    public TwoPageFlipperLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.TwoPageFlipperLayout,
                0, 0);
        try {
            firstPageLayout = a.getString(R.styleable.TwoPageFlipperLayout_first_page_layout);
            secondPageLayout = a.getString(R.styleable.TwoPageFlipperLayout_second_page_layout);
        } finally {
            a.recycle();
        }
        init(context,firstPageLayout,secondPageLayout);
    }

    public ViewFlipper getFlipper() { return flipper; }

    public View getFirstPage() {
        return firstPage;
    }

    public View getSecondPage() {
        return secondPage;
    }

    public void setViewFirstPage() { flipper.setDisplayedChild(0); }

    public void init (Context context, String firstPageLayout, String secondPageLayout) {
        rootView = inflate(context, R.layout.two_page_flipper, this);
        flipper = rootView.findViewById(R.id.flipper);

        int firstPageId = ResourceManager.getLayoutID(getContext(),firstPageLayout);
        int secondPageId = ResourceManager.getLayoutID(getContext(),secondPageLayout);

        ViewStub firstPageStub = rootView.findViewById(R.id.first_page_stub);
        firstPageStub.setLayoutResource(firstPageId);
        firstPage = firstPageStub.inflate();

        ViewStub secondPageStub = rootView.findViewById(R.id.second_page_stub);
        secondPageStub.setLayoutResource(secondPageId);
        secondPage = secondPageStub.inflate();

        flipButton = rootView.findViewById(R.id.expand_activities_button);
        flipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showNext();
            }
        });
        invalidate();
        requestLayout();
    }

}
