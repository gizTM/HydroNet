package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.SensorManagerActivity;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class NavigationManager {

    public static boolean navigateTo (Context context, int item) {
        switch (item) {
            case R.id.home :
//                context.startActivity(new Intent(context,HomeActivity.class));
                Toast.makeText(context,"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_plant :
//                context.startActivity(new Intent(context,MyProfileActivity.class));
                Toast.makeText(context,"My Plant",Toast.LENGTH_SHORT).show();
                break;
            case R.id.community :
                Toast.makeText(context,"Community",Toast.LENGTH_SHORT).show();
                break;
            case R.id.feed :
//                context.startActivity(new Intent(context,FeedActivity.class));
                Toast.makeText(context,"Feed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.trading :
//                context.startActivity(new Intent(context,TradingActivity.class));
                Toast.makeText(context,"Trading",Toast.LENGTH_SHORT).show();
                break;
            case R.id.netpie_console :
                context.startActivity(new Intent(context, SensorManagerActivity.class));
                Toast.makeText(context,"NetPie Console",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
        return false;
    }
    public static void showToast(Context context, String msg, int duration) {
        final Toast toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.show();
        (new CountDownTimer(duration, 500) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }
            public void onFinish() {
                toast.cancel();
            }

        }).start();
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
