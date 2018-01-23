package com.senior.gizgiz.hydronet.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomFloatingActionButton;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class FabActivity extends Activity {
    private static MaterialSheetFab materialSheetFab;
    private static LinearLayout addSystem,addUser,addNew;

    public static void initAddFAB(final Context context, final View view) {
        final CustomFloatingActionButton fab = view.findViewById(R.id.fab_add);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.dim_overlay);
        int sheetColor = view.getResources().getColor(R.color.bg_nav_menu);
        int fabColor = view.getResources().getColor(R.color.green);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        addSystem = view.findViewById(R.id.menu_add_from_system);
        addUser = view.findViewById(R.id.menu_add_from_user);
        addNew = view.findViewById(R.id.menu_add_new);

        addSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheet();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.popup_add_plant,null);
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                view.getRootView().findViewById(R.id.dim_popup_overlay).setVisibility(View.VISIBLE);
                view.getRootView().findViewById(R.id.dim_popup_overlay).setClickable(false);
                popup.setOutsideTouchable(false);
                View.OnClickListener dismissAddPopup = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                };
                customView.findViewById(R.id.btn_add_cancel).setOnClickListener(dismissAddPopup);
//                customView.findViewById(R.id.popup_add_close).setOnClickListener(dismissAddPopup);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        view.getRootView().findViewById(R.id.dim_popup_overlay).setVisibility(View.INVISIBLE);
                    }
                });
                popup.showAtLocation(customView, Gravity.CENTER,0,0);
            }
        });

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }
}
