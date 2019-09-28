package com.app.taskboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

class Run {
    private Activity currActivity;
    private Context currContext;
    private int currActivityRes;
    private DataOperation mDatabase;
    private TaskAdapter mTaskAdapter;
    private ClickRegistration register;
    private boolean isInitailized;
    final private Map<Integer, Activity> activityMap = new HashMap<>();
    final private Map<Integer, View>     layoutMap   = new HashMap<>();
    static Run obj;

    private Run() {
        register = new ClickRegistration();
    }

    static Run getRunObject() {
        if(obj == null)
            obj = new Run();
        return obj;
    }

    void setContext(Context context) {
        currContext = context;
    }

    void startUp(Activity activity) {
        isInitailized = true;
        UserPermission.checkStoragePermission(activity);
        mDatabase = new DataOperation();
        if( mDatabase.connect() ) mTaskAdapter = mDatabase.load(currContext);
        else {
            //TODO: unable to connect to database check for permissions or exit
        }
    }

    void setActivity(int activityRes, Activity activity, Context context) {
        currActivityRes = activityRes;
        currActivity = activity;
        activityMap.put(activityRes, activity);
    }

    void addLayout(int layoutRes, View v) {
        layoutMap.put(layoutRes, v);
    }

    void shutDown() {
        if(isInitailized) {
            mDatabase.save();
            mDatabase.disconnect();
            mTaskAdapter = null;
        }
    }

    ClickRegistration getRegister() {
        return register;
    }

    TaskAdapter getTaskAdapter() {
        return mTaskAdapter;
    }

    Context getContext() {
        return currContext;
    }

    Activity getActivity(int activityRes) {
        return activityMap.get(activityRes);
    }

    Activity getCurrActivity() {
        return currActivity;
    }

    View findViewByResource(int resId, int resource) {
        Activity mActivity = activityMap.get(resId);
        if(mActivity != null)
            return mActivity.findViewById(resource);

        View mView = layoutMap.get(resId);
        if(mView != null)
            return mView.findViewById(resource);

        return null;
    }
}
