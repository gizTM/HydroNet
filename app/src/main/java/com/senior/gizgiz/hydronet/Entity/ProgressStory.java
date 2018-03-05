package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProgressStory extends Story {
    private int historyNumber;

    public ProgressStory(User owner, String remark, Plant mentionedPlant,int historyNumber) {
        super(owner, StoryAdapter.getTypeProgress(),remark,mentionedPlant);
        this.historyNumber = historyNumber;
    }

    public int getHistoryNumber() { return historyNumber; }

    public void setHistoryNumber(int historyNumber) { this.historyNumber = historyNumber; }
}
