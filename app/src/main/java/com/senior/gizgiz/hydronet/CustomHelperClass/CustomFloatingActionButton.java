package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.gordonwong.materialsheetfab.AnimatedFab;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class CustomFloatingActionButton extends FloatingActionButton implements AnimatedFab {

    public CustomFloatingActionButton(Context context) { super(context); }
    public CustomFloatingActionButton(Context context, AttributeSet attrs) { super(context,attrs); }
    public CustomFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void show() {
        show(10,10);
    }

    @Override
    public void show(float translationX, float translationY) {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.INVISIBLE);
    }
}
