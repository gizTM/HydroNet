package com.senior.gizgiz.hydronet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.HelperClass.CustomButton;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class FeedFragment extends Fragment {
    private CustomButton shareBTN;
    private ListView storyList;
    private StoryAdapter storyAdapter;

    public FeedFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        shareBTN = view.findViewById(R.id.btn_share_new_story);
        storyList = view.findViewById(R.id.story_list);
        storyAdapter = new StoryAdapter(getContext(),StoryAdapter.exampleCards);
        storyList.setAdapter(storyAdapter);
    }
}
