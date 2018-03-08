package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.HelperClass.CustomButton;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private CustomButton shareBTN;
    private ListView storyList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private StoryAdapter storyAdapter;

    public FeedFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        shareBTN = view.findViewById(R.id.btn_share_new_story);
        storyList = view.findViewById(R.id.story_list);
        swipeRefreshLayout = view.findViewById(R.id.feed_swipe_layout);
        storyAdapter = new StoryAdapter(getContext());
        storyList.setAdapter(storyAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchStoryData(swipeRefreshLayout);
            }
        });
    }

    private void fetchStoryData(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
        StoryAdapter.stories.clear();
        RealTimeDBManager.getDatabase().child("stories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StoryAdapter.stories.add(dataSnapshot.getValue(Story.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildAdded","stories");
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                StoryAdapter.stories.remove(dataSnapshot.getValue(ProductAnnouncementStory.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildRemove","stories");
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
        RealTimeDBManager.getDatabase().child("progressstories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StoryAdapter.stories.add(dataSnapshot.getValue(ProgressStory.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildAdded","progressstories");
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                StoryAdapter.stories.remove(dataSnapshot.getValue(ProductAnnouncementStory.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildRemoved","progressstories");
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
        RealTimeDBManager.getDatabase().child("salestories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StoryAdapter.stories.add(dataSnapshot.getValue(ProductAnnouncementStory.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildAdded","salestories");
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                StoryAdapter.stories.remove(dataSnapshot.getValue(ProductAnnouncementStory.class));
                storyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("onChildRemoved","salestories");
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override public void onRefresh() { fetchStoryData(swipeRefreshLayout); }
}
