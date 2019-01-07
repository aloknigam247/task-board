package com.app.taskboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

class Run {
    private Activity mActivity;
    private Context mContext;
    private DataOperation mDatabase;
    private TaskList mTaskList;
    private boolean isInitailized;
    static final private Map<Integer, Activity> activityMap = new HashMap<>();

    Run(int activityRes, Activity activity, Context context) {
        mActivity = activity;
        mContext = context;
        addActivity(activityRes, activity);
    }

    void startUp() {
        isInitailized = true;
        UserPermission.checkStoragePermission(mActivity);
        mDatabase = new DataOperation();
        if( mDatabase.connect() ) mTaskList = mDatabase.load(mContext);
        else {
            //TODO: unable to connect to database check for permissions or exit
        }


    }

    void addActivity(int activityRes, Activity activity) {
        activityMap.put(activityRes, activity);
    }

    void shutDown() {
        if(isInitailized) {
            mDatabase.save();
            mDatabase.disconnect();
            mTaskList = null;
        }
    }

    TaskList getTaskList() {
        return mTaskList;
    }

    Context getContext() {
        return mContext;
    }

    static View findViewByActivity(int activityRes, int resource) {
        Activity mActivity = activityMap.get(activityRes);
        if(mActivity != null)
            return mActivity.findViewById(resource);
        return null;
    }
}
