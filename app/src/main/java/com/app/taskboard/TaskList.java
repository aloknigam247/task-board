package com.app.taskboard;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

class TaskList {
    //TaskList properties, how TaskList container contains Tasks
    private List<Task> mTaskList;       // contains list of tasks
    private TaskAdapter mTaskAdapter;   // TaskAdapter that implements View of Task
    private AdapterView taskView;       // Contains View of Task

    TaskList(Context context) {
        mTaskList = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(context);
        taskView = (AdapterView) Run.findViewByResource(R.layout.activity_main , R.id.taskDisplay);
        if(taskView != null) {
            taskView.setAdapter(mTaskAdapter.getAdapter());
        }
        else {
            //TODO: add case later
        }
    }

    //Operations on TaskList container
    //TODO: resolve multiple Adapter notification for data set changed while loading from data base
    void add(Task t){
        mTaskList.add(t);
        mTaskAdapter.add(t);
    }

    void remove(int index){
        if(index < 0 || index >= mTaskList.size())  return;
        mTaskAdapter.remove(getTask(index));    // Task should be removed from Adapter first, else it will be lost
        mTaskList.remove(index);
    }

    void clear() {
        mTaskList.clear();
        mTaskAdapter.clear();
    }

    int size() {
        return mTaskList.size();
    }

    Task getTask(int i) {
        return mTaskList.get(i);
    }

    void fillView (Context mContext) {
        mTaskAdapter.addAll(mTaskList, 0, mTaskList.size()-1);
    }

    int moveUp(int index) {
        if(index <= 0 || index >= mTaskList.size())  return index;
        Task temp = mTaskList.remove(index);
        mTaskList.add(index-1, temp);
        mTaskAdapter.moveUp(index);
        return index-1;
    }

    int moveDown(int index) {
        if(index < 0 || index >= mTaskList.size()-1)  return index;
        Task temp = mTaskList.remove(index);
        mTaskList.add(index+1, temp);
        mTaskAdapter.moveDown(index);
        return index+1;
    }
}

class Task {
    //Task properties
    private String name;

    Task(String n) {
        name = n;
    }

    //Operations on a Task
    String getName() {
        return name;
    }

    static Task readTask(BufferedReader readBuf) {
        try {
            String str = readBuf.readLine();
            if(str != null)
                return new Task(str);
        }
        catch(Exception e) {
            //TODO: add case later
        }
        return null;
    }

    static void writeTask(Task t, BufferedWriter writeBuf) {
        try {
            writeBuf.write(t.name, 0, t.name.length());
            writeBuf.write('\n');
        }
        catch (Exception e) {
            //TODO: add case later
        }
    }
}

//Implement task adapter that decides how task should view to user
class TaskAdapter {
    final static int LAYOUT_RES = R.layout.text_view_layout;
    final static int TEXTVIEW_RES = R.id.textView;
    private ArrayAdapter<String> mArrayAdapter;

    TaskAdapter(Context context) {
        mArrayAdapter = new ArrayAdapter<>(context, LAYOUT_RES, TEXTVIEW_RES);
    }

    BaseAdapter getAdapter() {
        return mArrayAdapter;
    }

    void add(Task t) {
        mArrayAdapter.add(t.getName());
        mArrayAdapter.notifyDataSetChanged();
    }

    void addAll(List<Task> mTaskList, int from, int to) {
        if(from < 0 || to >= mTaskList.size())
            return;
        for(int i=from; i<=to; i++)
            mArrayAdapter.add(mTaskList.get(i).getName());
        mArrayAdapter.notifyDataSetChanged();
    }

    void remove(Task t) {
        mArrayAdapter.remove(t.getName());
        mArrayAdapter.notifyDataSetChanged();
    }

    void removeAll(List<Task> mTaskList, int from, int to) {
        if(from < 0 || to >= mTaskList.size())
            return;
        for(int i = from; i<=to; i++)
            mArrayAdapter.remove(mTaskList.get(i).getName());
        mArrayAdapter.notifyDataSetChanged();
    }

    void clear() {
        mArrayAdapter.clear();
    }

    void moveUp(int index) {
        String temp = mArrayAdapter.getItem(index);
        mArrayAdapter.remove(temp);
        mArrayAdapter.insert(temp, index-1);
        mArrayAdapter.notifyDataSetChanged();
    }

    void moveDown(int index) {
        String temp = mArrayAdapter.getItem(index);
        mArrayAdapter.remove(temp);
        mArrayAdapter.insert(temp, index+1);
        mArrayAdapter.notifyDataSetChanged();
    }
}