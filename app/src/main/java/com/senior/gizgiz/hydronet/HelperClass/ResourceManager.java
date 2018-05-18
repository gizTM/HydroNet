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
import android.util.Log;

import com.senior.gizgiz.hydronet.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Admins on 016 16/1/2018.
 */

public class ResourceManager {
    private static Locale th = new Locale("th","TH");
    public static DateFormat noYearFormat = new SimpleDateFormat("d/M",th);
    public static DateFormat shortDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, th);
    public static DateFormat shortDateFormatForFileName = new SimpleDateFormat("dd-MM-yy",th);
    public static DateFormat weekFormat = new SimpleDateFormat("w",th);
    public static DateFormat shortDateTimeFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy", th);
    public static DecimalFormat twoDecimalPlaceFormat = new DecimalFormat("0.0#");

    public static int getDrawableID(Context context,String drawable) {
        return context.getResources().getIdentifier(drawable,"drawable",context.getPackageName());
    }

    public static int getLayoutID(Context context, String layout) {
        return context.getResources().getIdentifier(layout,"layout",context.getPackageName());
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

    public static void writeToFile(String data,Context context,String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context,String fileName) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
