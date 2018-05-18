package com.senior.gizgiz.hydronet.HelperClass;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;

/**
 * Created by Admins on 024 24/03/2018.
 */

public class PlantPopup extends BlurPopupWindow {
    private DecimalFormat decimalFormat = new DecimalFormat("0.0#");
    private Plant plant;

    public PlantPopup(@NonNull Context context) { super(context); }

    public void setPlant(Plant plant) { this.plant = plant; }

    @Override
    protected View createContentView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_plant, parent, false);
//        ((ImageView)view.findViewById(R.id.plant_thumbnail)).setImageResource(ResourceManager.getDrawableID(getContext(),"ic_plant_"+plant.getName()));
//        ((CustomTextView)view.findViewById(R.id.plant_name)).setText(plant.getName());
//        ((CustomTextView)view.findViewById(R.id.ph_range)).setText(decimalFormat.format(plant.getpHLow()).concat(" - ").concat(decimalFormat.format(plant.getpHHigh())));
//        ((CustomTextView)view.findViewById(R.id.ec_range)).setText(decimalFormat.format(plant.geteCLow()).concat(" - ").concat(decimalFormat.format(plant.geteCHigh())));
//        String spannableString = ResourceManager.getSeparateString(plant.getProperty());
//        SpannableStringBuilder bulletBuilder = new SpannableStringBuilder(spannableString);
//        ResourceManager.showBullet(bulletBuilder,spannableString);
//        ((CustomTextView)view.findViewById(R.id.plant_property)).setText(bulletBuilder);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);
        view.setVisibility(INVISIBLE);
        return view;
    }


    @Override
    protected void onShow() {
        super.onShow();
//        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                getContentView().setVisibility(VISIBLE);
//                int height = getContentView().getMeasuredHeight();
//                ObjectAnimator.ofFloat(getContentView(), "translationY", height, 0).setDuration(getAnimationDuration()).start();
//            }
//        });
    }

    @Override
    protected ObjectAnimator createShowAnimator() {
        return null;
    }

    @Override
    protected ObjectAnimator createDismissAnimator() {
        int height = getContentView().getMeasuredHeight();
        return ObjectAnimator.ofFloat(getContentView(), "translationY", 0, height).setDuration(getAnimationDuration());
    }

    public static class Builder extends BlurPopupWindow.Builder<PlantPopup> {
        private Plant plant;
        public Builder(Context context,Plant plant) {
            super(context);
            this.plant = plant;
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000);
        }

        public void setPlant(Plant plant) { this.plant = plant; }

        @Override
        protected PlantPopup createPopupWindow() {
            PlantPopup popup = new PlantPopup(mContext);
            popup.setPlant(plant);
            return popup;
        }
    }
}
