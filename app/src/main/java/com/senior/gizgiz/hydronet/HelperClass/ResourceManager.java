package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class ResourceManager {

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

    public static int getDim(Context context, int dim) {
        return context.getResources().getDimensionPixelSize(dim);
    }
}
