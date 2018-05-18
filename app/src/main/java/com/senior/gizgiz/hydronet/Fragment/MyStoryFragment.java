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
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyStoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private static ListView storyOverviewList;
    private static LinearLayout emptyState;
    private static CustomTextView overallStat;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean storyUpdated = false;
    private static StoryAdapter storyAdapter;
    private static boolean[] refreshed = new boolean[3],emptyList = new boolean[3];

    private static List<Story> allStories = new ArrayList<>();
    private static List<Story> stories = new ArrayList<>();
    private static List<Story> saleStories = new ArrayList<>();
    private static List<Story> progressStories = new ArrayList<>();

    final String uid = MainActivity.currentUser.getUid();
    Query storyQuery,progressQuery,saleQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_story, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        storyOverviewList = view.findViewById(R.id.my_story_list);
        swipeRefreshLayout = view.findViewById(R.id.my_story_swipe_layout);
        storyAdapter = new StoryAdapter(getContext(),allStories);
        storyOverviewList.setAdapter(storyAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.e("myStory","onViewCreated");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
//                if(!storyUpdated) {
//                    fetchUserStory(swipeRefreshLayout);
//                    storyUpdated = true;
//                }
            }
        });
        emptyState = view.findViewById(R.id.my_story_empty_state);
    }

    private void setupAllStories(String remark) {
        Log.e("--------------------","--------------------");
        Log.e("refresh",refreshed[0]+","+refreshed[1]+","+refreshed[2]);
        Log.e("emptyList",emptyList[0]+","+emptyList[1]+","+emptyList[2]);
        Log.e("my story size",stories.size()+","+progressStories.size()+","+saleStories.size());
        storyAdapter = new StoryAdapter(getActivity(),allStories);
        storyOverviewList.setAdapter(storyAdapter);
        Log.e("myStory-setupAllStories",remark);
        if(emptyList[0]&&emptyList[1]&&emptyList[2]) {
            emptyState.setVisibility(View.VISIBLE);
            allStories.clear();stories.clear();progressStories.clear();saleStories.clear();
            storyAdapter.notifyDataSetChanged();
        }
        else emptyState.setVisibility(View.GONE);
//        if(refreshed[0]&&refreshed[1]&&refreshed[2]) {
            allStories.clear();
            allStories.addAll(stories);
            allStories.addAll(progressStories);
            allStories.addAll(saleStories);
            Collections.sort(allStories, new Comparator<Story>() {
                public int compare(Story o1, Story o2) {
                    return (o2).getPostDate().compareTo((o1).getPostDate());
                }});
            storyAdapter.notifyDataSetChanged();
            Log.e("myStory count",allStories.size()+"");
//            refreshed[0] = refreshed[1] = refreshed[2] = true;
            refreshed = new boolean[3];
            emptyList = new boolean[3];
//        }
        Log.e("--------------------","--------------------");
    }
    private ValueEventListener postStoriesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                stories.clear();
                progressStories.clear();
                saleStories.clear();
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String storyId = childSnapshot.getKey();
                    RealTimeDBManager.getDatabase().child("stories").orderByKey().equalTo(storyId).addValueEventListener(storyValueListener);
                    RealTimeDBManager.getDatabase().child("progressStories").orderByKey().equalTo(storyId).addValueEventListener(progressValueListener);
                    RealTimeDBManager.getDatabase().child("saleStories").orderByKey().equalTo(storyId).addValueEventListener(saleValueListener);
                }
            } else {
                emptyList[0] = emptyList[1] = emptyList[2] = true;
                refreshed[0] = refreshed[1] = refreshed[2] = false;
                setupAllStories("postStory not exist");
            }
            swipeRefreshLayout.setRefreshing(false);
        }
        @Override public void onCancelled(DatabaseError databaseError) { }
    };
    private ValueEventListener storyValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                int i = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String storyId = childSnapshot.getKey();
                    Story story = childSnapshot.getValue(Story.class);
                    story.setId(storyId);
                    if (story.getLikedUsers().get(uid) != null)
                        story.setLiked(true);
                    stories.add(story);
                    storyAdapter.notifyDataSetChanged();
                    emptyState.setVisibility(View.GONE);
                    i++;
                    if (i == dataSnapshot.getChildrenCount()) {
                        emptyList[0] = false;
                        refreshed[0] = true;
                        setupAllStories("stories exist");
                    }
                }
            } else {
                emptyList[0] = true;
                refreshed[0] = true;
                setupAllStories("stories not exist");
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private ValueEventListener progressValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                int i = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    ProgressStory story = childSnapshot.getValue(ProgressStory.class);
                    if (story.getLikedUsers().get(uid) != null)
                        story.setLiked(true);
                    progressStories.add(story);
                    storyAdapter.notifyDataSetChanged();
                    emptyState.setVisibility(View.GONE);
                    i++;
                    if (i == dataSnapshot.getChildrenCount()) {
                        emptyList[1] = false;
                        refreshed[1] = true;
                        setupAllStories("progressStories exist");
                    }
                }
            } else {
                emptyList[1] = true;
                refreshed[1] = true;
                setupAllStories("progressStories not exist");
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private ValueEventListener saleValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final ProductAnnouncementStory story = childSnapshot.getValue(ProductAnnouncementStory.class);
                    final int historyNumber = story.getHistoryNumber();
                    final String storyId = childSnapshot.getKey();
                    story.setId(storyId);
                    story.setReserved(0);
                    if (story.getLikedUsers().get(uid) != null)
                        story.setLiked(true);
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
                                                            saleStories.add(story);
                                                            storyAdapter.notifyDataSetChanged();
                                                            emptyList[2] = false;
                                                            refreshed[2] = true;
                                                            setupAllStories("saleStories exist");
                                                        }
                                                    }
                                                } else {
                                                    saleStories.add(story);
                                                    storyAdapter.notifyDataSetChanged();
                                                    emptyList[2] = false;
                                                    refreshed[2] = true;
                                                    setupAllStories("negotiation not exist");
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
            } else {
                emptyList[2] = true;
                refreshed[2] = true;
                setupAllStories("saleStories not exist");
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private void fetchUserStory(final SwipeRefreshLayout swipeRefreshLayout) {
        final String uid = MainActivity.currentUser.getUid();
        Log.e("fetch my story",">>>");
        swipeRefreshLayout.setRefreshing(true);
//        refreshed[0] = refreshed[1] = refreshed[2] = false;
        RealTimeDBManager.getDatabase().child("postStories/"+uid).addListenerForSingleValueEvent(postStoriesListener);
    }

    @Override
    public void removeFirebaseListener() {
        RealTimeDBManager.getDatabase().child("postStories/"+MainActivity.currentUser.getUid()).removeEventListener(postStoriesListener);
        if(storyQuery!=null&&progressQuery!=null&&saleQuery!=null) {
            storyQuery.removeEventListener(storyValueListener);
            progressQuery.removeEventListener(progressValueListener);
            saleQuery.removeEventListener(saleValueListener);
        }
    }

    @Override
    public void fetchData() { fetchUserStory(swipeRefreshLayout); }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("my story","onPause");
        removeFirebaseListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("my story", "onResume");
//        if(!storyUpdated) {
//            fetchUserStory(swipeRefreshLayout);
//            storyUpdated = true;
//        }
    }

    @Override public void onRefresh() { fetchUserStory(swipeRefreshLayout); }
}
