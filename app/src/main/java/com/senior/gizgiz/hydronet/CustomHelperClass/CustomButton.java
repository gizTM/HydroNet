package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;

import com.senior.gizgiz.hydronet.R;


/**
 * Created by Admins on 015 15/01/2018.
 */

public class CustomButton extends AppCompatButton {
    private int fontAttr;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
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
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        setTypeface(tf);
        invalidate();
        requestLayout();
    }
}
