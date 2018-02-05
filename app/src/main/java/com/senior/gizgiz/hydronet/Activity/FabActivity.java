package com.senior.gizgiz.hydronet.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomFloatingActionButton;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 022 22/1/2018.
 */

public class FabActivity extends Activity {
    private static Context context;
    private static MaterialSheetFab materialSheetFab;
    private static LinearLayout addSystemFarm, addCustomFarm,addNew;

    public static void initAddFAB(Context c, final View view) {
        context = c;
        final CustomFloatingActionButton fab = view.findViewById(R.id.fab_add);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.dim_overlay);
        int sheetColor = view.getResources().getColor(R.color.bg_nav_menu);
        int fabColor = view.getResources().getColor(R.color.green);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        addSystemFarm = view.findViewById(R.id.menu_add_system_farm);
        addCustomFarm = view.findViewById(R.id.menu_add_custom_farm);

        addSystemFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheet();
                context.startActivity(new Intent(context, AddPlantActivity.class));
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View customView = inflater.inflate(R.layout.popup_add_plant,null);
//                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                view.getRootView().findViewById(R.id.dim_popup_overlay).setVisibility(View.VISIBLE);
//                view.getRootView().findViewById(R.id.dim_popup_overlay).setClickable(false);
//                popup.setOutsideTouchable(false);
//                customView.findViewById(R.id.btn_add_plant).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        DecimalFormat decimalFormat = new DecimalFormat("#");
//                        String count = ((CustomTextView)customView.findViewById(R.id.plant_count_badge)).getText().toString();
//                        int plantCount = new Integer(count.substring(1));
//                        plantCount = (plantCount>=32)?32:++plantCount;
//                        ((CustomTextView)customView.findViewById(R.id.plant_count_badge)).setText("x"+plantCount);
//                    }
//                });
//                customView.findViewById(R.id.btn_minus_plant).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        DecimalFormat decimalFormat = new DecimalFormat("#");
//                        String count = ((CustomTextView)customView.findViewById(R.id.plant_count_badge)).getText().toString();
//                        int plantCount = new Integer(count.substring(1));
//                        plantCount = (plantCount<=1)?1:--plantCount;
//                        ((CustomTextView)customView.findViewById(R.id.plant_count_badge)).setText("x"+plantCount);
//                    }
//                });
//                //spinner
//
//                View.OnClickListener dismissAddPopup = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        popup.dismiss();
//                    }
//                };
//                customView.findViewById(R.id.btn_add_cancel).setOnClickListener(dismissAddPopup);
////                customView.findViewById(R.id.popup_add_close).setOnClickListener(dismissAddPopup);
//                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        view.getRootView().findViewById(R.id.dim_popup_overlay).setVisibility(View.INVISIBLE);
//                    }
//                });
//                popup.showAtLocation(customView, Gravity.CENTER,0,0);
            }
        });

        addCustomFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,ExampleMultiSpinner.class));
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
