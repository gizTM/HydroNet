package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProgressStory extends Story {
    private int historyNumber;

    public ProgressStory() {}
    public ProgressStory(User owner, String remark, UserPlant mentionedPlant,int historyNumber) {
        super(owner,StoryAdapter.getTypeProgress(),remark,mentionedPlant);
        this.historyNumber = historyNumber;
    }

    public Map<String,Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("historynumber",historyNumber);
        return result;
    }

    public int getHistoryNumber() { return historyNumber; }

    public void setHistoryNumber(int historyNumber) { this.historyNumber = historyNumber; }
}
