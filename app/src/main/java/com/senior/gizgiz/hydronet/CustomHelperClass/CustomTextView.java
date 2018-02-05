package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.senior.gizgiz.hydronet.R;

public class CustomTextView extends AppCompatTextView {
    private int fontAttr;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        try {
            fontAttr = a.getInteger(R.styleable.CustomTextView_textStyle,0);
        } finally {
            a.recycle();
        }
        init(fontAttr);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        try {
            fontAttr = a.getInteger(R.styleable.CustomTextView_textStyle,0);
        } finally {
            a.recycle();
        }
        init(fontAttr);
        init(fontAttr);
    }

    public CustomTextView(Context context) {
        super(context);
        init(fontAttr);
    }

    private void init(int fontAttr) {
        String fontStyle = "Regular";
        if (fontAttr==0) fontStyle="Regular";
        else if (fontAttr==1) fontStyle = "ExtraLight";
        else if (fontAttr==2) fontStyle = "Light";
        else if (fontAttr==3) fontStyle = "Medium";
        else if (fontAttr==4) fontStyle = "Bold";
        else if (fontAttr==5) fontStyle = "SemiBold";
        else if (fontAttr==6) fontStyle = "Thin";
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/AdventPro/AdventPro-"+fontStyle+".ttf");
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSansDisplay-hinted/NotoSansDisplay-"+fontStyle+".ttf");
        setTypeface(tf);
        invalidate();
        requestLayout();
    }
}
