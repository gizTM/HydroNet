package com.senior.gizgiz.hydronet.Fragment.DetailFragment;

import android.content.Context;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.PlantOverviewAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.PlantOverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.BackPressHandler;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class PlantDetailFragment extends Fragment implements OnBackPressListener,SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private GridView plantList;
    private PlantAdapter plantAdapter;
    private PlantOverviewAdapter plantOverviewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyStateUserPlant;
    final DecimalFormat decimalFormat = new DecimalFormat("0.0#");

    private boolean plantUpdated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_plant_plant_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        plantList = view.findViewById(R.id.my_plant_list);
        plantAdapter = new PlantAdapter(getContext(),PlantAdapter.userPlants);
        plantOverviewAdapter = new PlantOverviewAdapter(getContext(), PlantAdapter.userPlants);
        swipeRefreshLayout = view.findViewById(R.id.plant_detail_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(!plantUpdated) {
                    fetchPlantData(swipeRefreshLayout, emptyStateUserPlant);
                    plantUpdated = true;
                }
            }
        });
        emptyStateUserPlant = view.findViewById(R.id.empty_state_userplant);
        plantList.setAdapter(plantAdapter);
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserPlant plant = PlantAdapter.userPlants.get(i);
                int numGrowing = 0,numHarvested = 0,numFailed = 0;
                for (GrowHistory history : plant.getGrowHistories()) {
                    if (history.getResult().equalsIgnoreCase("failed"))
                        numFailed += history.getCount();
                    else if (history.isHarvested()) numHarvested += history.getCount();
                    else numGrowing += history.getCount();
                }
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customView = inflater.inflate(R.layout.detail_plant,null);
                final PopupWindow popup = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                PlantPopup.Builder builder = new PlantPopup.Builder(getContext(),R.layout.detail_plant);
//                builder.setLayoutId(R.layout.detail_plant);
//                PlantPopup.Builder builder = new PlantPopup.Builder(getContext(),plant);
//                builder.setPlant(plant);
//                builder.setContentView(customView);
//                builder.build().show();
                popup.setOutsideTouchable(true);
                popup.showAtLocation(customView, Gravity.CENTER, 0, 0);
                NavigationManager.dimBehind(popup);
                ((ImageView)customView.findViewById(R.id.plant_thumbnail)).setImageResource(ResourceManager.getDrawableID(getContext(),"ic_plant_"+plant.getName()));
                ((CustomTextView)customView.findViewById(R.id.plant_name)).setText(plant.getName());
                ((CustomTextView)customView.findViewById(R.id.ph_range)).setText(decimalFormat.format(plant.getpHLow()).concat(" - ").concat(decimalFormat.format(plant.getpHHigh())));
                ((CustomTextView)customView.findViewById(R.id.ec_range)).setText(decimalFormat.format(plant.geteCLow()).concat(" - ").concat(decimalFormat.format(plant.geteCHigh())));
                ((CustomTextView)customView.findViewById(R.id.num_growing)).setText(String.valueOf(numGrowing));
                ((CustomTextView)customView.findViewById(R.id.num_harvested)).setText(String.valueOf(numHarvested));
                ((CustomTextView)customView.findViewById(R.id.num_failed)).setText(String.valueOf(numFailed));
                ((CustomTextView)customView.findViewById(R.id.plant_time)).setText(String.valueOf(plant.getGrowthDuration()));
                customView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });
                String spannableString = ResourceManager.getSeparateString(plant.getProperty());
                SpannableStringBuilder bulletBuilder = new SpannableStringBuilder(spannableString);
                ResourceManager.showBullet(bulletBuilder,spannableString);
                ((CustomTextView)customView.findViewById(R.id.plant_property)).setText(bulletBuilder);
                customView.findViewById(R.id.popup_plant).setOnClickListener(null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("plant detail","onResume");
        if(!plantUpdated) {
            fetchPlantData(swipeRefreshLayout, emptyStateUserPlant);
            plantUpdated = true;
        }
    }

    public void fetchData() {
        fetchPlantData(swipeRefreshLayout,emptyStateUserPlant);
    }

    @Override
    public void removeFirebaseListener() {

    }

    private void fetchPlantData(final SwipeRefreshLayout swipeRefreshLayout,final LinearLayout emptyState) {
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
//                                if (growing[0] + harvested[0] <= failed[0]) overallStat.setText(stat[2]);
//                                else overallStat.setText(stat[1]);
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
    @Override public boolean onBackPressed() {
        return new BackPressHandler(this).onBackPressed();
    }
    @Override public void onRefresh() { fetchPlantData(swipeRefreshLayout,emptyStateUserPlant); }
}
