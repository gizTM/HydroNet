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
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(drawable, "drawable",
                context.getPackageName());
        return resourceId;
//        return resources.getDrawable(resourceId);
    }

    public static Drawable getDrawable(Context context, String drawable) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(drawable, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
