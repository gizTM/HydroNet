package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TypefaceSpan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTypefaceSpan;
import com.senior.gizgiz.hydronet.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private String username;

    public static TypefaceSpan regularSpan, boldSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set typeface for text
//        Typeface regular = Typeface.createFromAsset(this.getAssets(), "fonts/AdventPro/AdventPro-Regular.ttf");
//        Typeface bold = Typeface.createFromAsset(this.getAssets(), "fonts/AdventPro/AdventPro-Bold.ttf");

//        regularSpan = new CustomTypefaceSpan(regular);
//        boldSpan = new CustomTypefaceSpan(bold);

        startActivity(new Intent(this, HomeActivity.class));
//        startActivity(new Intent(this, LoginActivity.class));

        // example code for database
//        database = FirebaseDatabase.getInstance();
//        databaseRef = database.getReference();
        // set data reference
//        databaseRef.setValue("message");
        // detect data change from firebase
        /*
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("OnDataChange", dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("OnCancelled", databaseError.toString());
            }
        });
        */
        // set data and child
//        username = "gizgiz";
//        databaseRef.child("users").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).setValue(username);

    }
}
