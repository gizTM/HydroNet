package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantGridViewAdapter;
import com.senior.gizgiz.hydronet.HelperClass.CustomFlipperLayout;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 007 07/02/2018.
 */

public class MaterialFragment extends FlipperFragment {
    private CustomFlipperLayout flipperLayout;
    private ListView materialList;
    private PlantGridViewAdapter materialAdapter;

    public MaterialFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_plant_material, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flipperLayout = view.findViewById(R.id.custom_material_flipper);
//        materialList = flipperLayout.findViewById(R.id.my_material_list);
//        materialAdapter = new PlantGridViewAdapter(getContext(),PlantGridViewAdapter.exampleSystemPlants);
//        materialList.setAdapter(materialAdapter);
//        ViewFlipper flipper = flipperLayout.findViewById(R.id.flipper);
//        if(flipper.getDisplayedChild()==1)
//            materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Plant card = PlantGridViewAdapter.exampleSystemPlants.get(i);
//                    Log.i("material list > ","material item selected");
//                    Toast.makeText(getContext(),card.getName()+"is selected",Toast.LENGTH_SHORT).show();
//                }
//            });
//        else {
//            Log.i("material list > ","expand detail");
//            flipperLayout.handleFlipButton();
//        }
    }

//    @Override public void setViewFirstPage() { flipperLayout.setViewFirstPage(); Log.i("material frag > ","setViewFirstPage"); }
//    @Override public void setViewSecondPage() { flipperLayout.setViewSecondPage(); }
//    @Override public void resetFlipper() { flipperLayout.resetFlipper(); }
}
