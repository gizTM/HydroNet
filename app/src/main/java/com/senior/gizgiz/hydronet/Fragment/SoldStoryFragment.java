package com.senior.gizgiz.hydronet.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class SoldStoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private static ListView storyOverviewList;
    private static LinearLayout emptyState;
    private static CustomTextView overallStat;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static StoryAdapter storyAdapter;
    private static List<Story> soldStories = new ArrayList<>();
    final String uid = MainActivity.currentUser.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_story, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storyOverviewList = view.findViewById(R.id.my_story_list);
        swipeRefreshLayout = view.findViewById(R.id.my_story_swipe_layout);
        storyAdapter = new StoryAdapter(getContext(), soldStories);
        storyOverviewList.setAdapter(storyAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.e("soldStory", "onViewCreated");
        emptyState = view.findViewById(R.id.my_story_empty_state);
    }

    @Override public void onRefresh() { fetchData(); }

    @Override
    public void fetchData() {
        soldStories.clear(); storyAdapter.notifyDataSetChanged();
        RealTimeDBManager.getDatabase().child("soldStories/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final String storyId = childSnapshot.getKey();
                        RealTimeDBManager.getDatabase().child("saleStories").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        if(childSnapshot.getKey().equalsIgnoreCase(storyId)) {
                                            final ProductAnnouncementStory story = childSnapshot.getValue(ProductAnnouncementStory.class);
                                            final int historyNumber = story.getHistoryNumber();
                                            final String storyId = childSnapshot.getKey();
                                            story.setId(storyId);
                                            story.setReserved(0);
                                            if (story.getLikedUsers().get(uid) != null) story.setLiked(true);
                                            RealTimeDBManager.getDatabase().child("growHistories/" + story.getMentionedPlantId()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        int index = 0;
                                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                            if (index == historyNumber) {
                                                                GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                                                List<GrowHistory> growHistories = new ArrayList<>();
                                                                growHistories.add(growHistory);
                                                                story.setMentionedUserPlant(new UserPlant(growHistory.getPlantName(), growHistories));
                                                                RealTimeDBManager.getDatabase().child("negotiations").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            int i = 0;
                                                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                                                final Negotiation negotiation = childSnapshot.getValue(Negotiation.class);
                                                                                story.addNegotiations(negotiation);
                                                                                story.addReserved(Integer.valueOf(negotiation.getCondition()));
                                                                                i++;
                                                                                if (i == dataSnapshot.getChildrenCount()) {
                                                                                    soldStories.add(story);
                                                                                    storyAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        } else {
                                                                            soldStories.add(story);
                                                                            storyAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                    }
                                                                });
                                                            }
                                                            index++;
                                                        }
                                                    } else Log.e("no dataa", ">>>");
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            emptyState.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                } else emptyState.setVisibility(View.VISIBLE);
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void removeFirebaseListener() {

    }
}
