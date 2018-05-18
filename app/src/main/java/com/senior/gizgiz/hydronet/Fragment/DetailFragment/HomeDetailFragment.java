package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.HistoryAdapter;
import com.senior.gizgiz.hydronet.ClassForList.HistoryCard;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admins on 010 10/02/2018.
 */

public class HomeDetailFragment extends Fragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private ListView historyList;
    private HistoryAdapter historyAdapter;
    private RelativeLayout warningLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView emptyState;
    private RadioGroup displayOption;

    private boolean historyUpdated = false;
    private List<HistoryCard> histories = new ArrayList<>();
    private Map<String,List<GrowHistory>> nowGrowing = new HashMap<>();
    private Map<String,List<GrowHistory>> harvested = new HashMap<>();
    private List<GrowHistory> growHistories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_home_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        warningLayout = view.findViewById(R.id.sensor_popup);
        warningLayout.findViewById(R.id.btn_close_warning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningLayout.setVisibility(View.GONE);
            }
        });
        historyList = view.findViewById(R.id.history_list);
        historyAdapter = new HistoryAdapter(getContext(), histories);
        swipeRefreshLayout = view.findViewById(R.id.history_detail_swipe_layout);
        emptyState = view.findViewById(R.id.empty_state_history);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.e("history","onViewCreated");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(!historyUpdated) {
                    fetchHistoryData(swipeRefreshLayout, emptyState);
                    historyUpdated = true;
                }
            }
        });
        historyList.setAdapter(historyAdapter);
        displayOption = view.findViewById(R.id.toggle_grow_harvested);
        displayOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setupList();
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("history","onResume");
        super.onResume();
        fetchHistoryData(swipeRefreshLayout,emptyState);
        historyUpdated = true;
    }

    public void fetchHistoryData(final SwipeRefreshLayout swipeRefreshLayout, final ImageView emptyState) {
        Log.e("fetch history",">>>");
        final String uid = MainActivity.currentUser.getUid();
        if(swipeRefreshLayout!=null) swipeRefreshLayout.setRefreshing(true);
        RealTimeDBManager.getDatabase().child("userPlants/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                histories.clear();
                nowGrowing.clear();
                harvested.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String upid = childSnapshot.getKey();
                        RealTimeDBManager.getDatabase().child("growHistories/" + upid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
//                                    growHistories.add(growHistory);
                                    if(!growHistory.isHarvested()) {
                                        if (nowGrowing.get(growHistory.getStartDate()) == null) {
                                            List<GrowHistory> histories = new ArrayList<>();
                                            histories.add(growHistory);
                                            nowGrowing.put(growHistory.getStartDate(), histories);
                                        } else nowGrowing.get(growHistory.getStartDate()).add(growHistory);
                                    } else {
                                        if (harvested.get(growHistory.getStartDate()) == null) {
                                            List<GrowHistory> histories = new ArrayList<>();
                                            histories.add(growHistory);
                                            harvested.put(growHistory.getStartDate(), histories);
                                        } else harvested.get(growHistory.getStartDate()).add(growHistory);
                                    }
                                    setupList();
                                }
                            }
                            @Override public void onCancelled(DatabaseError databaseError) {}
                        });
                        emptyState.setVisibility(View.GONE);
                    }
                } else emptyState.setVisibility(View.VISIBLE);
                if(swipeRefreshLayout!=null) swipeRefreshLayout.setRefreshing(false);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setupList() {
        histories.clear();
        if(displayOption.getCheckedRadioButtonId()==R.id.growing)
            for(Map.Entry<String,List<GrowHistory>> entry : nowGrowing.entrySet()) {
                histories.add(new HistoryCard("nowGrowing", entry.getValue()));
                historyAdapter.notifyDataSetChanged();
            }
        else
            for (Map.Entry<String,List<GrowHistory>> entry : harvested.entrySet()) {
                histories.add(new HistoryCard("harvested", entry.getValue()));
                historyAdapter.notifyDataSetChanged();
            }
    }

    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
    @Override public void onRefresh() { fetchHistoryData(swipeRefreshLayout,emptyState); }
}
