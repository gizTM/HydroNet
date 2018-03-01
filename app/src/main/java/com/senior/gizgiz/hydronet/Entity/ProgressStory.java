package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProgressStory extends Story {
    private String plantStatus;

    public ProgressStory(User owner, String plantStatus, String detail, Plant mentionedPlant) {
        super(owner, StoryAdapter.getTypeProgress(),detail,mentionedPlant);
        this.plantStatus = plantStatus;
    }
}
