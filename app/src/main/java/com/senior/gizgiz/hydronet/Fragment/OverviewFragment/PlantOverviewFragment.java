package com.senior.gizgiz.hydronet.Fragment.OverviewFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.PlantOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.PlantDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PlantOverviewFragment extends OverviewFragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener {
    private static ListView plantOverviewList;
    private static PlantOverviewAdapter plantOverviewAdapter;
    private static ImageView emptyState;
    private static CustomTextView overallStat;
    private SwipeRefreshLayout swipeRefreshLayout;

    private GestureDetector gestureDetector;

    private boolean plantUpdated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_plant_plant_overview, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        plantOverviewList = view.findViewById(R.id.plant_detail_list);
        plantOverviewAdapter = new PlantOverviewAdapter(getContext(), PlantAdapter.userPlants);
        plantOverviewList.setAdapter(plantOverviewAdapter);
        overallStat = view.findViewById(R.id.overall_stat);
        emptyState = view.findViewById(R.id.empty_state_userplant);
        swipeRefreshLayout = view.findViewById(R.id.plant_overview_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.e("plant overview","onViewCreated");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(!plantUpdated) {
                    fetchPlantData(swipeRefreshLayout, emptyState);
                    plantUpdated = true;
                }
            }
        });
        plantOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Plant plant = PlantAdapter.userPlants.get(i);
                Toast.makeText(getContext(), plant.getName()+" is selected!\n" +
                        "planted "+((UserPlant) plant).getGrowHistories().size()+" times!",Toast.LENGTH_SHORT).show();
            }
        });
        gestureDetector = new GestureDetector(getActivity(),new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                Log.e("onFling", "onFling has been called!");
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                        Log.e("onFling", "first if");
                        enterNextFragment();
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.e("onFling", "Right to Left");
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.e("onFling", "Left to Right");
                    }
                } catch (Exception e) {
                    // nothing
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        plantOverviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        view.findViewById(R.id.btn_show_detail_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("plant overview", "onResume");
        if(!plantUpdated) {
            fetchPlantData(swipeRefreshLayout, emptyState);
            plantUpdated = true;
        }
    }

    public static void fetchPlantData(final SwipeRefreshLayout swipeRefreshLayout,final ImageView emptyState) {
        Log.e("fetch plant",">>>");
        if(swipeRefreshLayout!=null) swipeRefreshLayout.setRefreshing(true);
        final String[] stat = {"a lot still in farm","looks good","too many dead plants"};
        RealTimeDBManager.getDatabase().child("userPlants/"+ MainActivity.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int[] growing = {0},harvested = {0},failed= {0};
                PlantAdapter.userPlants.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final UserPlant userPlant = childSnapshot.getValue(UserPlant.class);
                        RealTimeDBManager.getDatabase().child("growHistories/" + childSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<GrowHistory> growHistories = new ArrayList<>();
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                    growHistories.add(growHistory);
                                }
                                userPlant.setGrowHistories(growHistories);
                                growing[0] += plantOverviewAdapter.getStat().get(0);
                                harvested[0] += plantOverviewAdapter.getStat().get(1);
                                failed[0] += plantOverviewAdapter.getStat().get(2);
                                if (growing[0] + harvested[0] <= failed[0]) overallStat.setText(stat[2]);
                                else overallStat.setText(stat[1]);
                                PlantAdapter.userPlants.add(userPlant);
                                plantOverviewAdapter.notifyDataSetChanged();
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
    private void enterNextFragment() {
        PlantDetailFragment detailFragment = new PlantDetailFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_plant, detailFragment);
        transaction.commit();
    }

    public void backToOverview() {
        PlantOverviewFragment detailFragment = new PlantOverviewFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_plant, detailFragment);
        transaction.commit();
    }
    @Override public boolean setViewOverview() { return new BackPressHandler(this).onBackPressed(); }
    @Override public boolean onBackPressed() { return new BackPressHandler(this).onBackPressed(); }
    @Override public void onRefresh() {
        Log.e("plant overview","onRefresh");
        fetchPlantData(swipeRefreshLayout,emptyState);
    }
}
