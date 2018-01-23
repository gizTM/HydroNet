package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.HomeActivity;
import com.senior.gizgiz.hydronet.Activity.MicrogearConsoleActivity;
import com.senior.gizgiz.hydronet.Activity.MyPlantActivity;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 017 17/1/2018.
 */

public class NavigationManager {

    public static boolean navigateTo (Context context, int item) {
        switch (item) {
            case R.id.home :
                context.startActivity(new Intent(context,HomeActivity.class));
                Toast.makeText(context,"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_plant :
                context.startActivity(new Intent(context,MyPlantActivity.class));
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
                context.startActivity(new Intent(context, MicrogearConsoleActivity.class));
                Toast.makeText(context,"NetPie Console",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
        return false;
    }

}
