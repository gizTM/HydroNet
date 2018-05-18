package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class ValueStepper extends LinearLayout {
    private View rootView,decrease,increase;
    private CustomEditText valueET;
    private int value,minValue,maxValue;
    private boolean disabled;
    private Drawable background;

    private OnValueChangeListener listener;

    public interface OnValueChangeListener {
        void onValueChanged(int value);
    }

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
            value = a.getInteger(R.styleable.ValueStepper_value,0);
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
            value = a.getInteger(R.styleable.ValueStepper_value,0);
            disabled = a.getBoolean(R.styleable.ValueStepper_disabled,false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(final Context context) {
        listener = null;
        rootView = inflate(context, R.layout.value_stepper, this);
        background = (disabled)?ResourceManager.getDrawable(context,"bg_disabled_stepper") :
                ResourceManager.getDrawable(context,"bg_stepper");
        rootView.setBackground(background);
        decrease = rootView.findViewById(R.id.stepper_decrease);
        increase = rootView.findViewById(R.id.stepper_increase);
        valueET = rootView.findViewById(R.id.stepper_value);
        valueET.setEnabled(false);
        if(!disabled) {
            decrease.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(value>minValue) value -= 1;
                    valueET.setText(String.valueOf(value));
                    if(listener != null) listener.onValueChanged(value);
                }
            });
            increase.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(value<maxValue) value += 1;
                    valueET.setText(String.valueOf(value));
                    if(listener != null) listener.onValueChanged(value);
                }
            });
        }
        valueET.setText(String.valueOf(value));
        invalidate();
        requestLayout();
    }

    public int getValue() { return value; }
    public int getMinValue() { return minValue; }
    public int getMaxValue() { return maxValue; }

    public void setValue(int value) {
        this.value = value;
        valueET.setText(String.valueOf(value));
    }
    public void setMinValue(int minValue) { this.minValue = minValue; }
    public void setMaxValue(int maxValue) { this.maxValue = maxValue; }
    public void setActiveBackground() {
        this.background = ResourceManager.getDrawable(getContext(),"btn_small_corner_green");
        invalidate();
        requestLayout();
    }
    public void resetBackground() {
        this.background = ResourceManager.getDrawable(getContext(),"bg_stepper");
        invalidate();
        requestLayout();
    }

    public void setOnValueChangedListener(OnValueChangeListener listener) { this.listener = listener; }
}
