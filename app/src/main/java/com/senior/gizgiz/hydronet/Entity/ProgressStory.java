package com.senior.gizgiz.hydronet.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProgressStory extends Story {

    public ProgressStory(User owner, String detail, Plant mentionedPlant) {
        super(owner,"progress",detail,mentionedPlant);
    }
}
