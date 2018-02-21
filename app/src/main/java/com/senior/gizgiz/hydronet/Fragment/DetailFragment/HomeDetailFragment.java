package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.HomeCardAdapter;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 010 10/02/2018.
 */

public class HomeDetailFragment extends Fragment implements OnBackPressListener {
    private ListView homeCardList;
    private HomeCardAdapter homeCardAdapter;
    private RelativeLayout warningLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_home_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        warningLayout = view.findViewById(R.id.sensor_popup);
        warningLayout.findViewById(R.id.btn_close_warning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningLayout.setVisibility(View.GONE);
            }
        });
        homeCardList = view.findViewById(R.id.history_list);
        homeCardAdapter = new HomeCardAdapter(getContext(), HomeCardAdapter.exampleCards);
        homeCardList.setAdapter(homeCardAdapter);
        homeCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), HomeCardAdapter.exampleCards.get(i).getName()+" is selected!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
}
