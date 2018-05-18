package com.senior.gizgiz.hydronet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.Entity.Story;
import com.senior.gizgiz.hydronet.Fragment.FeedFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.Map;

public class ViewStoryActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private CustomTextView owner,postDate,detail,likeCount;
    private ImageView img,tryBTN,likeBTN,optionBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        this.owner = findViewById(R.id.owner_username);
        this.img = findViewById(R.id.feed_img);
        this.likeBTN = findViewById(R.id.btn_like);
        this.tryBTN = findViewById(R.id.btn_try);
        this.postDate = findViewById(R.id.post_date);
        this.likeCount = findViewById(R.id.like_count);
        this.detail = findViewById(R.id.story_detail);
        this.optionBTN = findViewById(R.id.btn_option);
        setup();
        handleStory();
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_sub_story);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void handleStory() {
        final Story card = getIntent().getExtras().getParcelable("STORY");
        this.owner.setText(card.getOwner().getUsername());
        this.postDate.setText(new TimeAgo().timeAgo(Long.valueOf(card.getPostDate())));
        this.tryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        new ContextThemeWrapper(ViewStoryActivity.this, R.style.myDialog));
                View dialogCustomLayout = LayoutInflater.from(ViewStoryActivity.this).inflate(R.layout.confirm_dialog, null);
                ((CustomTextView) dialogCustomLayout.findViewById(R.id.dialog_message)).setText("Want to grow this plant?");
                dialogBuilder.setView(dialogCustomLayout);
                final AlertDialog dialog = dialogBuilder.create();
                dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        Intent intent = new Intent(ViewStoryActivity.this, AddPlantActivity.class);
                        if(card.getMentionedPlantType() == 0) {
//                            intent.putExtra("PLANTNAME",card.getMentionedPlant().getName());
                            intent.putExtra("PLANT",card.getMentionedPlant());
                            intent.putExtra("TYPE",0);
                        }
                        else {
//                            intent.putExtra("PLANTNAME",card.getMentionedUserPlant().getName());
                            intent.putExtra("PLANT",card.getMentionedUserPlant());
                            intent.putExtra("TYPE",1);
                            intent.putExtra("UID",card.getOwnerId());
                        }
                        startActivity(intent);
                    }
                });
                dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        likeBTN.setImageResource(ResourceManager.getDrawableID(ViewStoryActivity.this,card.getLiked()?"ic_liked":"ic_like"));
        likeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setLiked(!card.getLiked());
                if(card.getLiked()) {
                    likeBTN.setImageResource(ResourceManager.getDrawableID(ViewStoryActivity.this,"ic_liked"));
                    RealTimeDBManager.likeStory(card.getId(),card.getOwnerId(),card.getType());
                } else {
                    likeBTN.setImageResource(ResourceManager.getDrawableID(ViewStoryActivity.this,"ic_like"));
                    RealTimeDBManager.unlikeStory(card.getId(),card.getOwnerId(),card.getType());
                }
            }
        });
        int likedCount = card.getLikedUsers().size();
        likeCount.setText(likedCount==0?"":String.valueOf(likedCount));
        String plantName;
        if(card.getMentionedPlantType() == 1) {
            plantName = card.getMentionedUserPlant().getName();
            Log.e("mentionedUserPlant",card.getMentionedUserPlant().getName());
        }
        else {
            plantName = card.getMentionedPlant().getName();
            Log.e("mentionedPlant",card.getMentionedPlant().getName());
        }
        Log.e("plant name",plantName);
        Glide.with(ViewStoryActivity.this)
                .load(ResourceManager.getDrawableID(ViewStoryActivity.this,"ic_plant_"+plantName))
                .fitCenter()
                .into(img);
        detail.setText(card.getRemark());
        if(card.getOwnerId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
            optionBTN.setVisibility(View.VISIBLE);
            optionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(ViewStoryActivity.this,v);
                    popup.getMenuInflater().inflate(R.menu.menu_item_option, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                                    new ContextThemeWrapper(ViewStoryActivity.this,R.style.myDialog));
                            View dialogCustomLayout = LayoutInflater.from(ViewStoryActivity.this).inflate(R.layout.confirm_dialog,null);
                            ((CustomTextView)dialogCustomLayout.findViewById(R.id.dialog_message)).setText(
                                    "Delete this story?");
                            dialogBuilder.setView(dialogCustomLayout);
                            final AlertDialog dialog = dialogBuilder.create();
                            dialogCustomLayout.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                    RealTimeDBManager.deleteStory(card.getType(),card.getId(),card.getOwnerId());
                                    FeedFragment.deleteStory(card.getType(),card.getId());
                                    finish();
                                }
                            });
                            dialogCustomLayout.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                            return true;
                        }
                    });
                }
            });
        } else optionBTN.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }
}
