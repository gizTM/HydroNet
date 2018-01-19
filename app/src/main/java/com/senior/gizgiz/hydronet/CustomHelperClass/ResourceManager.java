package com.senior.gizgiz.hydronet.CustomHelperClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Layout;

import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class ResourceManager {
    private static Context context = null;

    public static void init(Context c) {
        context = c;
    }

    public static int getDrawableID(Context context,String drawable) {
        return context.getResources().getIdentifier(drawable,"drawable",context.getPackageName());
//        return resources.getDrawable(resourceId);
    }

    public static int getLayoutID(Context context, String layout) {
        return context.getResources().getIdentifier(layout,"layout",context.getPackageName());
//        return resources.getLayout(resourceId);
    }

    public static Drawable getDrawable(Context context, String drawable) {
        int resourceId = context.getResources().getIdentifier(drawable,"drawable",context.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    public static int getID(Context context, String id) {
        return context.getResources().getIdentifier(id,"id",context.getPackageName());
    }

    public static String getString(Context context, String string) {
        int resourceId = context.getResources().getIdentifier(string,"string",context.getPackageName());
        return context.getResources().getString(resourceId);
    }
}
