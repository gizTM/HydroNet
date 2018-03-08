package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;

import com.senior.gizgiz.hydronet.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class ResourceManager {
    private static Locale th = new Locale("th","TH");
    public static DateFormat shortDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, th);
    public static DateFormat shortDateTimeFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy", th);

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

    public static int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
//        return context.getResources().getColor(color,context.getTheme());
    }

    public static String getSeparateString(String string) {
        String formattedString = "";
        for (String unit : string.split(";")) formattedString += unit+"\n";
        return formattedString;
    }

    public static void showBullet(SpannableStringBuilder builder, String textSeparatedByNextLine) {
        for (String string : textSeparatedByNextLine.split("\n")) {
            BulletSpan bulletSpan = new BulletSpan(30, Color.BLACK);
            builder.setSpan(bulletSpan, textSeparatedByNextLine.indexOf(string),
                    textSeparatedByNextLine.indexOf(string) + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
