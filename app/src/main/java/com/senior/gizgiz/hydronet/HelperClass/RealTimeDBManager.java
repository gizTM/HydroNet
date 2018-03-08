package com.senior.gizgiz.hydronet.HelperClass;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Adapter.GridViewAdapter.PlantAdapter;
import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;
import com.senior.gizgiz.hydronet.Entity.Item;
import com.senior.gizgiz.hydronet.Entity.Plant;
import com.senior.gizgiz.hydronet.Entity.ProductAnnouncementStory;
import com.senior.gizgiz.hydronet.Entity.ProgressStory;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Entity.SystemDefaultPlant;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.Entity.UserPlant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 005 05/03/2018.
 */

public class RealTimeDBManager {
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public static void writeNewUser(String username, String email, String password) {
        User user = new User(username,email,password);
        database.child("users").push().setValue(user);
    }

    public static void writeNewItem(String id, String name, float cost, String detail) {
//        String key = database.child("items").push().getKey();
        Item item = new Item(id,name,cost,detail);
        database.child("items").push().setValue(item);
//        Map<String,Object> childUpdates = new HashMap<>();
//        childUpdates.put("items/"+key,itemValues);
//        database.updateChildren(childUpdates);
    }
    public static void writeNewStory(String owner, int type, String remark, int plant, String conditionType, String condition) {
//        String key = database.child("stories").push().getKey();
//        Map<String,Object> childUpdates = new HashMap<>();
//        Map<String,Object> values;
        UserPlant mentionedPlant = (UserPlant)PlantAdapter.exampleUserPlants.get(plant);
        switch (type) {
            case 0:
                Story story = new Story(new User(owner),type,remark,mentionedPlant);
                database.child("stories").push().setValue(story);
                break;
            case 1:
                ProgressStory progressStory = new ProgressStory(new User(owner),remark,mentionedPlant,
                        StoryAdapter.createMockHistoryNumber(mentionedPlant));
                database.child("progressstories").push().setValue(progressStory);
                break;
            case 2:
                ProductAnnouncementStory announcementStory = new ProductAnnouncementStory(new User(owner),Integer.valueOf(conditionType),
                        condition,remark,mentionedPlant,StoryAdapter.createMockHistoryNumber(mentionedPlant));
                database.child("salestories").push().setValue(announcementStory);
                break;
        }
    }
    public static void writeNewPlant(String name,float pHLow,float pHHigh,float ECLow,float ECHigh) {
        SystemDefaultPlant systemDefaultPlant = new SystemDefaultPlant(name,pHLow,pHHigh,ECLow,ECHigh);
        database.child("systemplants").push().setValue(systemDefaultPlant);
    }

    public static DatabaseReference getDatabase() { return database; }
    public static FirebaseUser getCurrentUser() { return currentUser; }
    public static void setUpdateValueEventListener() { database.addValueEventListener(updateValueEventListener); }
    public static ValueEventListener updateValueEventListener = new ValueEventListener() {
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
