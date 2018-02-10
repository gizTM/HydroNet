package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class SingleExpandListLayout extends RelativeLayout {
    private int layoutCount;
    private List<String> title = new ArrayList<>();
    private List<LinearLayout> listLayouts = new ArrayList<>();
    private List<RelativeLayout> toggles = new ArrayList<>();
    private List<GridView> gridViews = new ArrayList<>();
    private List<CustomTextView> labelTitles = new ArrayList<>();

    public SingleExpandListLayout(Context context) {
        super(context);
        init(context);
    }
    public SingleExpandListLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SingleExpandListLayout,
                0, 0);
        try {
            layoutCount = a.getInteger(R.styleable.SingleExpandListLayout_numList,0);
            title.add(a.getString(R.styleable.SingleExpandListLayout_title1));
            title.add(a.getString(R.styleable.SingleExpandListLayout_title2));
            title.add(a.getString(R.styleable.SingleExpandListLayout_title3));
        } finally {
            a.recycle();
        }
        init(context);
    }
    public SingleExpandListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SingleExpandListLayout,
                0, 0);
        try {
            layoutCount = a.getInteger(R.styleable.SingleExpandListLayout_numList,0);
            title.add(a.getString(R.styleable.SingleExpandListLayout_title1));
            title.add(a.getString(R.styleable.SingleExpandListLayout_title2));
            title.add(a.getString(R.styleable.SingleExpandListLayout_title3));
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        View customLayout = inflate(context, R.layout.custom_layout_expand_list, this);
        for (int i=0; i<layoutCount; i++) {
            listLayouts.add((LinearLayout) customLayout);
            toggles.add((RelativeLayout) customLayout.findViewById(R.id.toggle_expand));
            gridViews.add((GridView) customLayout.findViewById(R.id.list));
            customLayout.findViewById(R.id.label_title);
        }
        invalidate();
        requestLayout();
    }

    public static void handleSingleExpand(View customView, int toggleBTN, int expandLayout, boolean isExpanded) {
        if (isExpanded) {
            expand(customView.findViewById(expandLayout));
            customView.findViewById(toggleBTN).setBackgroundResource(R.drawable.ic_expand_less);
        } else {
            collapse(customView.findViewById(expandLayout));
            customView.findViewById(toggleBTN).setBackgroundResource(R.drawable.ic_expand_more);
        }
    }
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
