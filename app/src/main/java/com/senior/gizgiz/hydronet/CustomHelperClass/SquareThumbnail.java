package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class SquareThumbnail extends RelativeLayout {
    public SquareThumbnail(Context context) { super(context); }

    public SquareThumbnail(Context context, AttributeSet attrs) { super(context,attrs); }

    public SquareThumbnail(Context context, AttributeSet attrs, int defStyle) { super(context,attrs,defStyle); }

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }
}
