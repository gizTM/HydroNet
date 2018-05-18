package com.senior.gizgiz.hydronet.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.AddProgressStoryActivity;
import com.senior.gizgiz.hydronet.Activity.AddSaleStoryActivity;
import com.senior.gizgiz.hydronet.Activity.AddStoryActivity;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.ViewSaleStoryActivity;
import com.senior.gizgiz.hydronet.Activity.ViewStoryActivity;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.NotificationAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.Entity.Notification;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Admins on 019 19/02/2018.
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static ListView storyListView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private CustomTextView addNewStoryBTN;
    private static LinearLayout emptyState;

    private static StoryAdapter storyAdapter;
    private boolean storyUpdated = false,addStoryExpand = true;
    private static boolean[] refreshed = new boolean[3],emptyList = new boolean[3];

    private static List<Story> allStories = new ArrayList<>();
    private static Map<String,Story>  stories = new HashMap<>();
    private static Map<String,Story>  progressStories = new HashMap<>();
    private static Map<String,Story> saleStories = new HashMap<>();

    private GestureDetector gestureDetector;

    public FeedFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        storyListView = view.findViewById(R.id.story_list);
        swipeRefreshLayout = view.findViewById(R.id.feed_swipe_layout);
        addNewStoryBTN = view.findViewById(R.id.btn_share_new_story);
        storyAdapter = new StoryAdapter(getActivity(),allStories);
        storyListView.setAdapter(storyAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.e("feed","onViewCreated");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
//                if(!storyUpdated) {
                    fetchStoryData(swipeRefreshLayout);
//                    storyUpdated = true;
//                }
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
                final int SWIPE_MAX_OFF_PATH = 200;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                        if(addStoryExpand && e1.getY()>e2.getY()) {
                            Log.e("onFling", "up");
                            NavigationManager.collapse(addNewStoryBTN);
                            addStoryExpand = false;
                        } else if(!addStoryExpand) {
                            Log.e("onFling", "down");
                            NavigationManager.expand(addNewStoryBTN);
                            addStoryExpand = true;
                        }
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
//        storyListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return gestureDetector.onTouchEvent(motionEvent);
//            }
//        });
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = allStories.get(position);
                Intent intent = new Intent(getContext(),ViewStoryActivity.class);;
                 if(story.getType()==0) {
                    intent = new Intent(getContext(),ViewStoryActivity.class);
                    intent.putExtra("STORY",story);
                }
                else if(story.getType()==1) {
                    intent = new Intent(getContext(),ViewStoryActivity.class);
                    ProgressStory progressStory = (ProgressStory) allStories.get(position);
                    intent.putExtra("STORY",progressStory);
                }
                else {
                    intent = new Intent(getContext(),ViewSaleStoryActivity.class);
                    ProductAnnouncementStory saleStory = (ProductAnnouncementStory) allStories.get(position);
                    intent.putExtra("STORY",saleStory);
                }
                startActivity(intent);
            }
        });
        addNewStoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View bottomSheetView = inflater.inflate(R.layout.bottom_sheet_share_type, null);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetView.findViewById(R.id.story_type_normal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), AddStoryActivity.class));
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetView.findViewById(R.id.story_type_progress).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), AddProgressStoryActivity.class));
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetView.findViewById(R.id.story_type_sale).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), AddSaleStoryActivity.class));
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetDialog.show();
            }
        });
        emptyState = view.findViewById(R.id.empty_state_feed);
    }

    @Override public void onResume() {
        super.onResume();
        Log.e("feed","onResume");
//        if(!storyUpdated) {
            fetchStoryData(swipeRefreshLayout);
//            storyUpdated = true;
//        }
    }
    private ValueEventListener storyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                stories.clear();
                int i=0;
                String uid = MainActivity.currentUser.getUid();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String storyId = childSnapshot.getKey();
                    Story story = childSnapshot.getValue(Story.class);
                    story.setId(storyId);
                    if(story.getLikedUsers().get(uid) != null) story.setLiked(true);
                    if(stories.get(storyId)==null) stories.put(storyId,story);
                    storyAdapter.notifyDataSetChanged();
                    emptyState.setVisibility(View.GONE);
                    i++;
                    if(i==dataSnapshot.getChildrenCount()) {
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
        @Override public void onCancelled(DatabaseError databaseError) {}
    };
    private ValueEventListener progressStoryListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                progressStories.clear();
                int i=0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String uid = MainActivity.currentUser.getUid();
                    String storyId = childSnapshot.getKey();
                    ProgressStory story = childSnapshot.getValue(ProgressStory.class);
                    if(story.getLikedUsers().get(uid) != null) story.setLiked(true);
                    if(progressStories.get(storyId)==null) progressStories.put(storyId,story);
                    storyAdapter.notifyDataSetChanged();
                    emptyState.setVisibility(View.GONE);
                    i++;
                    if(i==dataSnapshot.getChildrenCount()) {
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
        @Override public void onCancelled(DatabaseError databaseError) {}
    };
    private ValueEventListener saleStoryListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                saleStories.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String uid = MainActivity.currentUser.getUid();
                    final ProductAnnouncementStory story = childSnapshot.getValue(ProductAnnouncementStory.class);
                    final int historyNumber = story.getHistoryNumber();
                    final String storyId = childSnapshot.getKey();
                    final boolean[] likeChanged = {false};
                    story.setId(storyId);
                    story.setReserved(0);
                    if(story.getLikedUsers().get(uid) != null) story.setLiked(true);
                    RealTimeDBManager.getDatabase().child("growHistories/"+story.getMentionedPlantId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                int index = 0;
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    if(index==historyNumber) {
                                        GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                        List<GrowHistory> growHistories = new ArrayList<>();
                                        growHistories.add(growHistory);
                                        story.setMentionedUserPlant(new UserPlant(growHistory.getPlantName(),growHistories));
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
                                                        if (!likeChanged[0] && i == dataSnapshot.getChildrenCount()) {
                                                            if (saleStories.get(storyId) == null) saleStories.put(storyId, story);
                                                            storyAdapter.notifyDataSetChanged();
                                                            emptyList[2] = false;
                                                            refreshed[2] = true;
                                                            setupAllStories("negotiation exist");
                                                        }
                                                    }
                                                } else if(!likeChanged[0]) {
                                                    if(saleStories.get(storyId)==null) saleStories.put(storyId,story);
                                                    storyAdapter.notifyDataSetChanged();
                                                    emptyList[2] = false;
                                                    refreshed[2] = true;
                                                    setupAllStories("negotiation not exist");
                                                }
                                            }
                                            @Override public void onCancelled(DatabaseError databaseError) {}
                                        });
                                    }
                                    index++;
                                }
                            } else Log.e("no dataa",">>>");
                        }
                        @Override public void onCancelled(DatabaseError databaseError) {}
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
        @Override public void onCancelled(DatabaseError databaseError) {}
    };
    public static void addNegotiation(int resultCode, Intent data) {
        Log.e("feedFragment", "onActivityResult");
        if (resultCode == RESULT_OK) {
            Log.e("feedFragment","RESULT_OK");
            String ownerId = data.getStringExtra("OWNER_ID");
            String uid = data.getStringExtra("NEGOTIATORID");
            String username = data.getStringExtra("NEGOTIATOR");
            String condition = data.getStringExtra("RESERVED");
            String storyId = data.getStringExtra("STORY_ID");
            String allReserved = data.getStringExtra("ALL_RESERVED");
            Negotiation negotiation = new Negotiation();
            negotiation.setCondition(condition);
            negotiation.setNegotiator(username);
            negotiation.setNegotiatorId(uid);
            negotiation.setStatus("reserved");
            negotiation.setStoryId(storyId);
            String now = String.valueOf(new Date().getTime());
            negotiation.setTimestamp(now);
            for(Story story : allStories) {
                if (story instanceof ProductAnnouncementStory && story.getId().equalsIgnoreCase(storyId)) {
                    Log.e("onActivityResult","update certain story");
                    String id = RealTimeDBManager.writeNewNegotiation(storyId,ownerId,username,uid,condition,allReserved,now);
                    negotiation.setId(id);
                    ((ProductAnnouncementStory) story).addNegotiations(negotiation);
                    storyAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    public static void updateNegotiation(int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            Log.e("feedFragment","RESULT_OK");
            final String uid = data.getStringExtra("NEGOTIATORID");
            final String username = data.getStringExtra("NEGOTIATOR");
            final String condition = data.getStringExtra("RESERVED");
            final String storyId = data.getStringExtra("STORY_ID");
            final String allReserved = data.getStringExtra("ALL_RESERVED");
            Log.e("allReserved",allReserved);
            final Negotiation negotiation = new Negotiation();
            negotiation.setCondition(condition);
            negotiation.setNegotiatorId(uid);
            negotiation.setNegotiator(username);
            negotiation.setStatus("reserved");
            negotiation.setStoryId(storyId);
            final String now = String.valueOf(new Date().getTime());
            negotiation.setTimestamp(now);
            for(Story story : allStories) {
                if (story instanceof ProductAnnouncementStory && story.getId().equalsIgnoreCase(storyId)) {
//                    Log.e("onActivityResult","update certain negotiation");
                    RealTimeDBManager.getDatabase().child("negotiations").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                int reserved = 0;
                                String negoKey = "";
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Negotiation nego = childSnapshot.getValue(Negotiation.class);
                                    reserved += Integer.valueOf(nego.getCondition());
                                    if(nego.getNegotiatorId().equalsIgnoreCase(uid)) {
                                        negoKey = childSnapshot.getKey();
//                                        Log.e("condition",condition);
//                                        negotiation.setCondition(condition);
//                                        storyAdapter.notifyDataSetChanged();
//                                        RealTimeDBManager.writeExistingNegotiation(storyId,childSnapshot.getKey(),condition,reserved,now);
//                                        break;
                                    }
                                }
                                if(negoKey!="") {
                                    negotiation.setCondition(condition);
                                    storyAdapter.notifyDataSetChanged();
                                    RealTimeDBManager.writeExistingNegotiation(storyId, negoKey, condition, reserved, now);
                                }
                            }
                        }
                        @Override public void onCancelled(DatabaseError databaseError) { }
                    });
                    break;
                }
            }
        }
    }
    private void setupAllStories(String remark) {
        Log.e("--------------------","--------------------");
//        Log.e("refresh",refreshed[0]+","+refreshed[1]+","+refreshed[2]);
        Log.e("emptyList",emptyList[0]+","+emptyList[1]+","+emptyList[2]);
        Log.e("my story size",stories.size()+","+progressStories.size()+","+saleStories.size());
        Log.e("setupAllStories",remark);
        storyAdapter = new StoryAdapter(getActivity(),allStories);
        storyListView.setAdapter(storyAdapter);
        if(emptyList[0]&&emptyList[1]&&emptyList[2]) {
            emptyState.setVisibility(View.VISIBLE);
            allStories.clear();stories.clear();progressStories.clear();saleStories.clear();
            storyAdapter.notifyDataSetChanged();
        }
        else emptyState.setVisibility(View.GONE);
//        if(refreshed[0]&&refreshed[1]&&refreshed[2]) {
            allStories.clear();
            allStories.addAll(stories.values());
            allStories.addAll(progressStories.values());
            allStories.addAll(saleStories.values());
            Collections.sort(allStories, new Comparator<Story>() {
                public int compare(Story o1, Story o2) {
                    return (o2).getPostDate().compareTo((o1).getPostDate());
                }});
            storyListView.setAdapter(storyAdapter);
            storyAdapter.notifyDataSetChanged();
//            int position = storyListView.getFirstVisiblePosition();
//            storyListView.setSelection(position + 1);
            Log.e("feed count",allStories.size()+"");
            refreshed = new boolean[3];
//            emptyList = new boolean[3];
//        }
        Log.e("--------------------","--------------------");
    }
    public static void deleteStory(int storyType, String storyId) {
        if(storyType==StoryAdapter.getTypeStory()) stories.remove(storyId);
        else if(storyType==StoryAdapter.getTypeProgress()) progressStories.remove(storyId);
        else saleStories.remove(storyId);
        storyAdapter.notifyDataSetChanged();
        allStories.clear();
        allStories.addAll(stories.values());
        allStories.addAll(progressStories.values());
        allStories.addAll(saleStories.values());
        Collections.sort(allStories, new Comparator<Story>() {
            public int compare(Story o1, Story o2) {
                return (o2).getPostDate().compareTo((o1).getPostDate());
            }});
        storyListView.setAdapter(storyAdapter);
        storyAdapter.notifyDataSetChanged();
    }
    public void fetchStoryData(final SwipeRefreshLayout swipeRefreshLayout) {
        Log.e("fetch story",">>>");
        swipeRefreshLayout.setRefreshing(true);
//        refreshed[0] = refreshed[1] = refreshed[2] = false;
        RealTimeDBManager.getDatabase().child("stories").addValueEventListener(storyListener);
        RealTimeDBManager.getDatabase().child("progressStories").addValueEventListener(progressStoryListener);
        RealTimeDBManager.getDatabase().child("saleStories").addValueEventListener(saleStoryListener);
    }
    private void removeFirebaseListener() {
        RealTimeDBManager.getDatabase().child("stories").removeEventListener(storyListener);
        RealTimeDBManager.getDatabase().child("progressStories").removeEventListener(progressStoryListener);
        RealTimeDBManager.getDatabase().child("saleStories").removeEventListener(saleStoryListener);
    }
    @Override public void onRefresh() { fetchStoryData(swipeRefreshLayout); }
}
