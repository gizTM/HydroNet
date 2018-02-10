package com.senior.gizgiz.hydronet.HelperClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.senior.gizgiz.hydronet.ClassForList.DropdownItem;
import com.senior.gizgiz.hydronet.R;

import java.util.List;

/**
 * Created by Admins on 003 3/2/2018.
 */

public class DropdownMenu extends LinearLayout {
    private View rootView;
    private String prompt;
    private boolean disabled;
    private PopupWindow popup;
    private List<DropdownItem> dropdownItemList;

    public DropdownMenu(Context context) {
        super(context);
        init(context);
    }
    public DropdownMenu(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.DropdownMenu,
                0, 0);
        try {
            prompt = a.getString(R.styleable.DropdownMenu_prompt);
            disabled = a.getBoolean(R.styleable.ValueStepper_disabled,false);
        } finally {
            a.recycle();
        }
        init(context);
    }
    public DropdownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.DropdownMenu,
                0, 0);
        try {
            prompt = a.getString(R.styleable.DropdownMenu_prompt);
            disabled = a.getBoolean(R.styleable.ValueStepper_disabled,false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(final Context context) {
//        this.dropdownItemList = list;
        rootView = inflate(context, R.layout.dropdown_menu, this);
        invalidate();
        requestLayout();
    }

    public PopupWindow createLocationPopup(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View customView = inflater.inflate(R.layout.popup_location,null);
        View customView = LayoutInflater.from(context).inflate(R.layout.popup_location,null);
        PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        ((ViewGroup)customView.getParent()).removeView(customView);
//        popup.showAtLocation(customView, Gravity.TOP,0,0);
//        popup.showAsDropDown(customView, 0, -customView.getHeight()+popup.getHeight(),Gravity.CENTER);
//        popup.update();
        this.popup = popup;
        return popup;
    }
}
