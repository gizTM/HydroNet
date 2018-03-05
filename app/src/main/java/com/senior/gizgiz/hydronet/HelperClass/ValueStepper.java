package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class ValueStepper extends LinearLayout {
    private View rootView,decrease,increase;
    private CustomEditText valueET;
    private float value;
    private boolean disabled;
    private Drawable background;

    public ValueStepper(Context context) {
        super(context);
        init(context);
    }
    public ValueStepper(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.ValueStepper,
                0, 0);
        try {
            value = a.getFloat(R.styleable.ValueStepper_value,0);
            disabled = a.getBoolean(R.styleable.ValueStepper_disabled,false);
        } finally {
            a.recycle();
        }
        init(context);
    }
    public ValueStepper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.ValueStepper,
                0, 0);
        try {
            value = a.getFloat(R.styleable.ValueStepper_value,0);
            disabled = a.getBoolean(R.styleable.ValueStepper_disabled,false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(final Context context) {
        final DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        rootView = inflate(context, R.layout.value_stepper, this);
        background = (disabled)?ResourceManager.getDrawable(context,"bg_disabled_stepper") :
                ResourceManager.getDrawable(context,"bg_stepper");
        rootView.setBackground(background);
        decrease = rootView.findViewById(R.id.stepper_decrease);
        increase = rootView.findViewById(R.id.stepper_increase);
        valueET = rootView.findViewById(R.id.stepper_value);
        if(!disabled) {
            decrease.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    value -= 0.1;
                    valueET.setText(decimalFormat.format(value));
                }
            });
            increase.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    value += 0.1;
                    valueET.setText(decimalFormat.format(value));
                }
            });
        }
        valueET.setText(decimalFormat.format(value));
        invalidate();
        requestLayout();
    }
}
