package com.senior.gizgiz.hydronet.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.senior.gizgiz.hydronet.Activity.AboutActivity;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 004 04/03/2018.
 */

public class UserFragment extends Fragment {

    public UserFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        view.findViewById(R.id.btn_license).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AboutActivity.class));
            }
        });
    }
}
