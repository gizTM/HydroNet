package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.SoldItemAdapter;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class SellFragment extends Fragment {
    private ListView soldList;
    private SoldItemAdapter soldItemAdapter;

    public SellFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        soldList = view.findViewById(R.id.simple_list);
        soldItemAdapter = new SoldItemAdapter(getContext(),SoldItemAdapter.exampleCards);
        soldList.setAdapter(soldItemAdapter);
    }
}
