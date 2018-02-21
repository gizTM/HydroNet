package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.MaterialAdapter;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class MaterialDetailFragment extends Fragment implements OnBackPressListener {
    private ListView materialList;
    private MaterialAdapter materialAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plant_material_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        materialList = view.findViewById(R.id.my_material_list);
        materialAdapter = new MaterialAdapter(getContext(),MaterialAdapter.exampleCards);
        materialList.setAdapter(materialAdapter);
        materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),MaterialAdapter.exampleCards.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
}
