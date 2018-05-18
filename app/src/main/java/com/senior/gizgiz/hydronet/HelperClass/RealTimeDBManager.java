package com.senior.gizgiz.hydronet.HelperClass;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.senior.gizgiz.hydronet.Entity.Notification;

/**
 * Created by Admins on 005 05/03/2018.
 */

public class RealTimeDBManager {
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public static void writeNewUser(String username, String email, String password) {
        String uid = MainActivity.currentUser.getUid();
        User user = new User(username,email,password);
        database.child("users").child(uid).setValue(user);
    }
    public static void writeNewItem(String id, String name, float cost, String detail) {
        Item item = new Item(id,name,cost,detail);
        database.child("items").push().setValue(item);
    }
    public static void writeNewSystemPlant(String name, int growthDuration,float pHLow, float pHHigh, float ECLow, float ECHigh) {
        SystemDefaultPlant systemDefaultPlant = new SystemDefaultPlant(name,growthDuration,pHLow,pHHigh,ECLow,ECHigh);
        database.child("systemPlants").push().setValue(systemDefaultPlant);
    }
    public static void writeNewSystemPlantStory(User owner, int type, String remark, String pid, Plant plant) {
        String uid = MainActivity.currentUser.getUid();
        Story story = new Story(owner,type,remark,plant,String.valueOf(new Date().getTime()));
        String storyId = database.child("stories").push().getKey();
        story.setId(storyId);
        database.child("stories/"+storyId).setValue(story);
        database.child("stories/"+storyId+"/ownerId").setValue(MainActivity.currentUser.getUid());
        database.child("stories/"+storyId+"/mentionedPlantType").setValue(0);
        database.child("stories/"+storyId+"/mentionedPlantId").setValue(pid);
        database.child("postStories/"+uid+"/"+storyId).setValue(true);
    }
    public static void writeNewUserPlantStory(User owner, int type, String remark, String pid, UserPlant userPlant) {
        String uid = MainActivity.currentUser.getUid();
        Story story = new Story(owner,type,remark,userPlant,String.valueOf(new Date().getTime()));
        String storyId = database.child("stories").push().getKey();
        story.setId(storyId);
        database.child("stories/"+storyId).setValue(story);
        database.child("stories/"+storyId+"/ownerId").setValue(MainActivity.currentUser.getUid());
        database.child("stories/"+storyId+"/mentionedPlantType").setValue(1);
        database.child("stories/"+storyId+"/mentionedPlantId").setValue(pid);
        database.child("postStories/"+uid+"/"+storyId).setValue(true);
    }
    public static void writeNewSaleStory(User owner, int type, String remark, String upid, final int historyNumber, int conditionType, String condition, String dueDate) {
        String uid = MainActivity.currentUser.getUid(), storyId;
        switch (type) {
            case 1:
                storyId = database.child("progressStories").push().getKey();
                ProgressStory progressStory = new ProgressStory(owner,remark,historyNumber,String.valueOf(new Date().getTime()));
                database.child("progressStories").push().setValue(progressStory);
                database.child("postStories/"+uid+"/"+storyId).setValue(true);
                break;
            case 2:
                UserPlant up = new UserPlant();
                up.setId(upid);
                ProductAnnouncementStory announcementStory = new ProductAnnouncementStory(owner,conditionType,
                        condition,remark,historyNumber,dueDate,String.valueOf(new Date().getTime()));
                storyId = database.child("saleStories").push().getKey();
                announcementStory.setId(storyId);
                database.child("saleStories/"+storyId).setValue(announcementStory);
                database.child("saleStories/"+storyId+"/ownerId").setValue(MainActivity.currentUser.getUid());
                database.child("saleStories/"+storyId+"/mentionedPlantType").setValue(1);
                database.child("saleStories/"+storyId+"/mentionedPlantId").setValue(upid);
                database.child("postStories/"+uid+"/"+storyId).setValue(true);
                break;
        }
    }
    public static void likeStory(String storyId,String notifiedUid,int type) {
        Notification notification = new Notification();
        String timeStamp = String.valueOf(new Date().getTime());
        String uid = MainActivity.currentUser.getUid(), username = MainActivity.username;
        String notificationKey = database.child("notifications/"+notifiedUid).push().getKey();
        notification.setType(1);
        database.child("likedStories/"+MainActivity.currentUser.getUid()+"/"+storyId).setValue(true);
        switch (type) {
            case 0 :
                database.child("stories/"+storyId+"/likedUsers/"+uid).setValue(true);
                notification.setMessage(username.concat(" liked your story"));
                notification.setStoryType(0);
                break;
            case 1 :
                database.child("progressStories/"+storyId+"/likedUsers/"+uid).setValue(true);
                notification.setMessage(username.concat(" liked your progress story"));
                notification.setStoryType(1);
                break;
            case 2 :
                database.child("saleStories/"+storyId+"/likedUsers/"+uid).setValue(true);
                notification.setMessage(username.concat(" liked your sale story"));
                notification.setStoryType(2);
                break;
        }
        notification.setSenderId(uid);
        notification.setTimeStamp(timeStamp);
        notification.setStoryId(storyId);
        database.child("notifications/"+notifiedUid+"/"+notificationKey).setValue(notification);
    }
    public static void unlikeStory(String storyId,String notifiedUid,int type) {
        String uid = MainActivity.currentUser.getUid();
        database.child("likedStories/"+uid+"/"+storyId).setValue(null);
        switch (type) {
            case 0 :
                database.child("stories/"+storyId+"/likedUsers/"+uid).setValue(null);
                break;
            case 1 :
                database.child("progressStories/"+storyId+"/likedUsers/"+uid).setValue(null);
                break;
            case 2 :
                database.child("saleStories/"+storyId+"/likedUsers/"+uid).setValue(null);
                break;
        }
    }
    public static void deleteStory(int storyType,String storyId,String ownerId) {
        switch (storyType) {
            case 0 :
                database.child("stories/"+storyId).setValue(null);
                database.child("postStories/"+ownerId).orderByKey().equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                database.child("likedStories").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                break;
            case 1 :
                database.child("progressStories/"+storyId).setValue(null);
                database.child("postStories/"+ownerId).orderByKey().equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                database.child("likedStories").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                break;
            case 2 :
                database.child("saleStories/"+storyId).setValue(null);
                database.child("postStories/"+ownerId).orderByKey().equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                database.child("likedStories").orderByChild("storyId").equalTo(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            childSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
                break;
        }
    }
    public static void writeExistingUserPlant(String name,String upid, int count, List<String> locationList, int growthDuration,String startDate) {
//        String uid = MainActivity.currentUser.getUid();
        GrowHistory growHistory = new GrowHistory(name,startDate,false,count,locationList);
        growHistory.setExpectedHarvestDate(String.valueOf(Long.valueOf(startDate)+growthDuration * 86400000L));
        growHistory.setPlantId(upid);
//        Log.e("expected harvest",startDate+"+"+growthDuration*86400000+"="+(growthDuration*86400000+Long.valueOf(startDate)));
//        database.child("userPlants/"+uid+"/"+upid+"/growHistories/"+index).setValue(growHistory);
        database.child("growHistories/"+upid).push().setValue(growHistory);
    }
    public static void writeNewUserPlant(String name, int count, List<String> locationList, int growthDuration,float pHLow, float pHHigh, float ECLow, float ECHigh, String startDate, String property) {
//        List<GrowHistory> growHistories = new ArrayList<>();
        String uid = MainActivity.currentUser.getUid();
        String upid = database.child("userPlants").child(uid).push().getKey();
        GrowHistory growHistory = new GrowHistory(name,startDate,false,count,locationList);
        growHistory.setExpectedHarvestDate(String.valueOf(Long.valueOf(startDate)+ growthDuration * 86400000L));
        growHistory.setPlantId(upid);
//        Log.e("expected harvest",startDate+"+"+growthDuration*86400000L+"="+(growthDuration*86400000L+Long.valueOf(startDate)));
//        growHistories.add(growHistory);
        UserPlant userPlant = new UserPlant(name,growthDuration,pHLow,pHHigh,ECLow,ECHigh,property,"");
        database.child("userPlants/"+uid+"/"+upid).setValue(userPlant);
        database.child("growHistories/"+upid).push().setValue(growHistory);
    }
    public static void writeNewSensorData(String date, float waterLevel,float pHLevel,float ecLevel) {
        final String uid = MainActivity.currentUser.getUid();
        final SensorData sensorData = new SensorData(date,waterLevel,pHLevel,ecLevel);
        final String sensorDataKey = database.child("sensorData/"+uid).push().getKey();
        database.child("sensorData/"+uid+"/"+sensorDataKey).setValue(sensorData);
        RealTimeDBManager.getDatabase().child("userPlants/" + uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String upid = childSnapshot.getKey();
                    RealTimeDBManager.getDatabase().child("growHistories/" + upid).orderByChild("harvested").equalTo(false).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i=0;
                            for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                final String ghid = childSnapshot.getKey();
                                database.child("sensorHistory/"+sensorDataKey+"/"+ghid).setValue(true);
                                database.child("growHistories/"+upid+"/"+ghid+"/sensorData/"+sensorDataKey).setValue(true);
                            }
                        }
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
    public static String writeNewNegotiation(String storyId,String notifiedUid, String negotiator,String negotiatorId,String condition,String reserved,String timeStamp) {
        String uid = MainActivity.currentUser.getUid();
        String negotiationKey = "";
        if(!condition.equalsIgnoreCase("0")) {
            negotiationKey = database.child("negotiations").push().getKey();
            database.child("saleStories/" + storyId + "/negotiation/" + negotiationKey).setValue(true);
            database.child("saleStories/" + storyId + "/reserved").setValue(Integer.valueOf(condition) + Integer.valueOf(reserved));
            database.child("saleStories/" + storyId + "/reservedUsers/" + uid).setValue(true);
            database.child("negotiations/" + negotiationKey + "/id").setValue(negotiationKey);
            database.child("negotiations/" + negotiationKey + "/negotiatorId").setValue(negotiatorId);
            database.child("negotiations/" + negotiationKey + "/negotiator").setValue(negotiator);
            database.child("negotiations/" + negotiationKey + "/timestamp").setValue(timeStamp);
            database.child("negotiations/" + negotiationKey + "/condition").setValue(condition);
            database.child("negotiations/" + negotiationKey + "/status").setValue("reserved");
            database.child("negotiations/" + negotiationKey + "/storyId").setValue(storyId);
            Notification notification = new Notification();
            String notificationKey = database.child("notifications/" + notifiedUid).push().getKey();
            notification.setMessage(negotiator + " made a reservation on your sale!");
            notification.setType(1);
            notification.setSenderId(uid);
            notification.setTimeStamp(timeStamp);
            notification.setStoryId(storyId);
            notification.setStoryType(2);
            database.child("notifications/" + notifiedUid + "/" + notificationKey).setValue(notification);
        }
        return negotiationKey;
    }
    public static void writeExistingNegotiation(String storyId,String negotiationId,String condition,int reserved,String timestamp) {
        if(condition.equalsIgnoreCase("0")) {
            database.child("negotiations/").setValue(null);
        } else {
            database.child("negotiations/" + negotiationId + "/condition").setValue(condition);
            database.child("saleStories/" + storyId + "/reserved").setValue(reserved);
            database.child("negotiations/" + negotiationId + "/timestamp").setValue(timestamp);
        }
    }
    public static void writeNewSensorDataToHistory(String sensorDataKey, Map<String,List<String>> upidToghids) {
        for(Map.Entry<String,List<String>> entry : upidToghids.entrySet()) {
            for(String ghid : entry.getValue())
                database.child("growHistories/"+entry.getKey()+"/"+ghid+"/sensorData/"+sensorDataKey).setValue(true);
        }
    }
    public static void writeNotificationRead(String notificationId) {
        database.child("notifications/"+MainActivity.currentUser.getUid()+"/"+notificationId+"/read").setValue(true);
    }
    public static void writeTransaction(List<String> notifiedIds, String storyId, String plantName,String ownerId,String plantOwner,String count) {
        String uid = MainActivity.currentUser.getUid();
        String timeStamp = String.valueOf(new Date().getTime());
        for(String notifiedId : notifiedIds) {
            String notificationKey = database.child("notifications/"+notifiedId).push().getKey();
            Notification notification = new Notification();
            notification.setId(notificationKey);
            notification.setType(1);
            notification.setSenderId(uid);
            notification.setTimeStamp(timeStamp);
            notification.setStoryId(storyId);
            notification.setMessage(plantOwner.concat("'s sale reservation period had ended, you got ").concat(count).concat(" ").concat(plantName).concat("s. \nBe sure to contact ").concat(plantOwner).concat(" for purchase and delivery detail."));
            notification.setStoryType(2);
            database.child("notifications/"+notifiedId+"/"+notificationKey).setValue(notification);
            database.child("boughtStories/"+notifiedId+"/"+storyId).setValue(true);
        }
        database.child("soldStories/"+ownerId+"/"+storyId).setValue(true);
        String notificationKey = database.child("notifications/"+ownerId).push().getKey();
        Notification notification = new Notification();
        notification.setId(notificationKey);
        notification.setType(1);
        notification.setSenderId(uid);
        notification.setTimeStamp(timeStamp);
        notification.setStoryId(storyId);
        notification.setMessage("Your sale reservation period had ended \nMake sure to contact all reservers for purchase and delivery detail.");
        notification.setStoryType(2);
        database.child("notifications/"+ownerId+"/"+notificationKey).setValue(notification);
    }
    public static void writeGrowResult(String upid,String ghid,List<String> failedLocations,List<String> harvestLocations) {
        database.child("growHistories/"+upid+"/"+ghid+"/harvested").setValue(true);
        database.child("growHistories/" + upid+"/"+ghid+"/failedList").setValue(failedLocations);
        database.child("growHistories/" + upid+"/"+ghid+"/harvestList").setValue(harvestLocations);
    }

    public static void remove(String child) { database.child(child).removeValue(); }
    public static void push(String child) { database.child(child).push().child("key").setValue("value"); }
    public static void pushWithUID(String child) { database.child(child).child(MainActivity.currentUser.getUid()).child("key").setValue("value"); }
    public static DatabaseReference getDatabase() { return database; }
    public static void setUpdateValueEventListener() { database.addValueEventListener(updateValueEventListener); }
    private static ValueEventListener updateValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("OnDataChange", dataSnapshot.toString());
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("OnCancelled", databaseError.toString());
        }
    };

}
