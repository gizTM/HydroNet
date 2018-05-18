package com.senior.gizgiz.hydronet.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.ViewSaleStoryActivity;
import com.senior.gizgiz.hydronet.Activity.ViewStoryActivity;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.NotificationAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.Entity.Notification;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.UserPlant;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Refreshable {
    private ListView notificationList;
    private NotificationAdapter notificationAdapter;
    private LinearLayout emptyState;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Notification> notifications = new ArrayList<>();
    private int selectedFilter = 0;

    public NotificationFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomTextView)view.findViewById(R.id.list_title)).setText(ResourceManager.getString(getContext(),"label_notification"));
        notificationList = view.findViewById(R.id.simple_list);
        notificationAdapter = new NotificationAdapter(getContext(),notifications);
        notificationList.setAdapter(notificationAdapter);
        swipeRefreshLayout = view.findViewById(R.id.simple_list_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        fetchData();
        view.findViewById(R.id.btn_read_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Notification notification : notifications) {
                    notification.setRead(true);
                    notificationAdapter.notifyDataSetChanged();
                    RealTimeDBManager.writeNotificationRead(notification.getId());
                }
            }
        });
        view.findViewById(R.id.btn_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.inflate(R.menu.menu_notification_option);
                Log.e("selectedFilter",selectedFilter+"");
                if(selectedFilter==0) popup.getMenu().findItem(R.id.option_all).setChecked(true);
                else if(selectedFilter==1) popup.getMenu().findItem(R.id.option_read).setChecked(true);
                else if(selectedFilter==2) popup.getMenu().findItem(R.id.option_not_read).setChecked(true);
                else if(selectedFilter==3) popup.getMenu().findItem(R.id.option_sensor).setChecked(true);
                else if(selectedFilter==4) popup.getMenu().findItem(R.id.option_story).setChecked(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_all :
                                selectedFilter = 0;
                                fetchData();
                                return true;
                            case R.id.option_read :
                                selectedFilter = 1;
                                fetchData();
                                return true;
                            case R.id.option_not_read :
                                selectedFilter = 2;
                                fetchData();
                                return true;
                            case R.id.option_sensor :
                                selectedFilter = 3;
                                fetchData();
                                return true;
                            case R.id.option_story :
                                selectedFilter = 4;
                                fetchData();
                                return true;
                            default :
                                selectedFilter = 0;
                                fetchData();
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = notifications.get(position);
                notification.setRead(true);
                RealTimeDBManager.writeNotificationRead(notification.getId());
                Intent intent = new Intent(getContext(),ViewStoryActivity.class);
                switch (notification.getType()) {
                    case NotificationAdapter.TYPE_STORY :
                        if(notification.getStoryType()==0) {
                            intent = new Intent(getContext(),ViewStoryActivity.class);
                            Story story = notification.getStory();
                            intent.putExtra("STORY",story);
                        }
                        else if(notification.getStoryType()==1) {
                            intent = new Intent(getContext(),ViewStoryActivity.class);
                            ProgressStory story = notification.getProgressStory();
                            intent.putExtra("STORY",story);
                        }
                        else {
                            intent = new Intent(getContext(),ViewSaleStoryActivity.class);
                            ProductAnnouncementStory saleStory = notification.getSaleStory();
                            intent.putExtra("STORY",saleStory);
                        }
                        break;
                    case NotificationAdapter.TYPE_SENSOR_WARNING :
                        intent = new Intent(getContext(),MainActivity.class);
                        break;

                }
                startActivity(intent);
            }
        });
        emptyState = view.findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);
    }

    @Override
    public void fetchData() {
        final String uid = MainActivity.currentUser.getUid();
        swipeRefreshLayout.setRefreshing(true);
        RealTimeDBManager.getDatabase().child("notifications/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    notifications.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final Notification notification = childSnapshot.getValue(Notification.class);
                        notification.setId(childSnapshot.getKey());
                        Log.e("message",notification.getMessage());
                        Log.e("type",notification.getType()+"");
                        switch (notification.getType()) {
                            case NotificationAdapter.TYPE_STORY:
                                if(selectedFilter==0 || selectedFilter==4) {
                                    if (notification.getStoryType() == 0) {
                                        Log.e("type", "story");
                                        RealTimeDBManager.getDatabase().child("stories").orderByKey().equalTo(notification.getStoryId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    int i = 0;
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        Story story = childSnapshot.getValue(Story.class);
                                                        if (story.getLikedUsers().get(uid) != null) story.setLiked(true);
                                                        Log.e("story", "exist");
                                                        notification.setStory(story);
                                                        i++;
//                                                        if (selectedFilter == 0 || selectedFilter == 4 ||
//                                                                (selectedFilter==1 && notification.isRead()) ||
//                                                                (selectedFilter==2 && !notification.isRead())) {
                                                        if(selectedFilter==0 || selectedFilter==4) {
                                                            notifications.add(notification);
                                                            notificationAdapter.notifyDataSetChanged();
                                                        }
                                                        if (i == dataSnapshot.getChildrenCount()) {
                                                            Collections.sort(notifications, new Comparator<Notification>() {
                                                                @Override
                                                                public int compare(Notification o1, Notification o2) {
                                                                    return (o2).getTimeStamp().compareTo((o1).getTimeStamp());
                                                                }
                                                            });
                                                            notificationAdapter.notifyDataSetChanged();
                                                            Log.e("notification size",notifications.size()+"");
                                                        }
                                                    }
                                                } else {
                                                    Log.e("story", "not exist");
                                                }
                                            }
                                            @Override public void onCancelled(DatabaseError databaseError) { }
                                        });
                                    } else if (notification.getStoryType() == 1) {
                                        Log.e("type", "progress");
                                        RealTimeDBManager.getDatabase().child("progressStories").orderByKey().equalTo(notification.getStoryId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        ProgressStory story = childSnapshot.getValue(ProgressStory.class);
                                                        Log.e("progress story", "exist");
                                                        notification.setStory(story);
                                                        notifications.add(notification);
                                                        notificationAdapter.notifyDataSetChanged();
                                                    }
                                                } else {
                                                    Log.e("progress story", "not exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        Log.e("type", "sale");
                                        RealTimeDBManager.getDatabase().child("saleStories").orderByKey().equalTo(notification.getStoryId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        final ProductAnnouncementStory story = childSnapshot.getValue(ProductAnnouncementStory.class);
                                                        Log.e("sale story", "exist");
                                                        Log.e("plant type", story.getMentionedPlantType() + "");
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
//                                                                                                if(selectedFilter==0 || selectedFilter==4 ||
//                                                                                                        (selectedFilter==1 && notification.isRead()) ||
//                                                                                                        (selectedFilter==2 && !notification.isRead())) {
                                                                                                    notification.setSaleStory(story);
                                                                                                    notifications.add(notification);
                                                                                                    notificationAdapter.notifyDataSetChanged();
//                                                                                                }
                                                                                                Log.e("negotiations", story.getNegotiations().size() + "");
                                                                                                Collections.sort(notifications, new Comparator<Notification>() {
                                                                                                    @Override
                                                                                                    public int compare(Notification o1, Notification o2) {
                                                                                                        return (o2).getTimeStamp().compareTo((o1).getTimeStamp());
                                                                                                    }
                                                                                                });
                                                                                                notificationAdapter.notifyDataSetChanged();
                                                                                                Log.e("notification size",notifications.size()+"");
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        notification.setSaleStory(story);
                                                                                        notifications.add(notification);
                                                                                        Collections.sort(notifications, new Comparator<Notification>() {
                                                                                            @Override
                                                                                            public int compare(Notification o1, Notification o2) {
                                                                                                return (o2).getTimeStamp().compareTo((o1).getTimeStamp());
                                                                                            }
                                                                                        });
                                                                                        notificationAdapter.notifyDataSetChanged();
                                                                                        Log.e("notification size",notifications.size()+"");
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
                                                    }
                                                } else {
                                                    Log.e("sale story", "not exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                break;
                            case NotificationAdapter.TYPE_SENSOR_WARNING:
//                                if(selectedFilter==0 || selectedFilter==3 ||
//                                        (selectedFilter==1 && notification.isRead()) ||
//                                        (selectedFilter==2 && !notification.isRead())) {
                                if(selectedFilter==0 || selectedFilter==3) {
                                    notifications.add(notification);
                                    Collections.sort(notifications, new Comparator<Notification>() {
                                        @Override
                                        public int compare(Notification o1, Notification o2) {
                                            return (o2).getTimeStamp().compareTo((o1).getTimeStamp());
                                        }
                                    });
                                    notificationAdapter.notifyDataSetChanged();
                                    Log.e("notification size",notifications.size()+"");
                                }
                                break;
                        }
                    }
                } else emptyState.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void removeFirebaseListener() {

    }

    @Override public void onRefresh() {
        fetchData();
    }
}

