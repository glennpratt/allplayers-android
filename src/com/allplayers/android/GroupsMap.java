package com.allplayers.android;

import com.allplayers.objects.DataObject;
import com.allplayers.objects.GroupData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GroupsMap {
    private ArrayList<GroupData> groups = new ArrayList<GroupData>();

    public GroupsMap(String jsonResult) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonResult.length(); i++) {
                    GroupData group = new GroupData(jsonArray.getString(i));

                    if (group.isNew(groups)) {
                        groups.add(group);
                    }
                }
            }
        } catch (JSONException ex) {
            System.err.println("GroupsMap/" + ex);
            System.err.println(jsonResult);
        }
    }

    public ArrayList<GroupData> getGroupData() {
        return groups;
    }
}