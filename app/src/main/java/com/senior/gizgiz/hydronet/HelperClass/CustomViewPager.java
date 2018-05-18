package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewPager extends ViewPager {
    PagerAdapter mPagerAdapter;

    public CustomViewPager(Context context) { super(context); }
    public CustomViewPager(Context context, AttributeSet attrs) { super(context, attrs); }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }

    private View findScrollingChild(View view) {
        if (view instanceof NestedScrollingChild) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0, count = group.getChildCount(); i < count; i++) {
                View scrollingChild = findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }

//    public boolean canScrollHorizontally (ViewPager view) {
//        boolean canScroll = false;
//        if (view !=null && view.getChildCount ()> 0) {
//            boolean isOnTop = view.() != 0 || view.getChildAt(0).getTop() != 0;
//            boolean isAllItemsVisible = isOnTop && view.getLastVisiblePosition() == view.getChildCount();
//
//            if (isOnTop || isAllItemsVisible) {
//                canScroll = true;
//            }
//        }
//        return  canScroll;
//    }

}
