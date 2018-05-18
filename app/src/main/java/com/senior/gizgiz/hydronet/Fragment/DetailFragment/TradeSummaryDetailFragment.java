package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 026 26/02/2018.
 */

public class TradeSummaryDetailFragment extends Fragment implements OnBackPressListener {
    private ListView tradeList;
    private CustomTextView stat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_trade_summary_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        tradeList = view.findViewById(R.id.trade_detail_list);

    }

    public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
}
