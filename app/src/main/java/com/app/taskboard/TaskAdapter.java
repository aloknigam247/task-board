package com.app.taskboard;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.Exception;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

class TaskAdapter extends BaseAdapter {
    private List<Task> mTaskList;       // contains list of tasks
    private int ItemClicked;
    private TaskForm mTaskForm;

    //TODO: load TaskAdapter

    TaskAdapter(Context context) {
        mTaskList = new ArrayList<>();
        ItemClicked = -1;
        mTaskForm = new TaskForm();
        mTaskForm.inflateTaskForm(context);

        Run.getRunObject().getRegister().registerResource(
                R.layout.activity_main,
                R.id.taskDisplay,
                (AdapterView<?> parent, View view, int position, long id) -> {
                        setItemClicked(position);
                    }
        );
    }

    /*
     * BaseAdapter Implementation
     */
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return mTaskList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return mTaskList.get(position).getTaskFace();
    }

    /*
     * Task Operations
     */
    void add(Task task) {
        mTaskList.add(task);
        notifyDataSetChanged();
    }

    void addFromFile(Task task) {
        mTaskList.add(task);
    }

    void remove(int position) {
        if(position < 0 || position >= mTaskList.size())  return;
        mTaskList.remove(position);
        notifyDataSetChanged();
    }

    void moveUp(int position) {
        if(position <= 0 || position >= mTaskList.size())  return;

        Task temp = mTaskList.get(position);
        mTaskList.remove(position);
        mTaskList.add(position-1, temp);
        setItemClicked(position-1);

        notifyDataSetChanged();
    }

    void moveDown(int position) {
        if(position < 0 || position >= mTaskList.size()-1)  return;

        Task temp = mTaskList.get(position);
        mTaskList.remove(position);
        mTaskList.add(position+1, temp);
        setItemClicked(position+1);

        notifyDataSetChanged();
    }

    void setItemClicked(int position) {
        if(position < 0 || position >= mTaskList.size())
            return;

        if(isItemClicked())
            mTaskList.get(ItemClicked).removeSelection();

        ItemClicked = position;
        mTaskList.get(ItemClicked).select();
    }

    int getItemClickedId() {
        return ItemClicked;
    }

    Task getItemClicked() {
        return mTaskList.get(ItemClicked);
    }

    boolean isItemClicked() {
        return ItemClicked >= 0;
    }

    void showNewForm() {
        mTaskForm.newTaskForm();
    }

    void editClickedTask() {
        mTaskForm.editTaskForm(mTaskList.get(ItemClicked));
    }
}

class TaskForm {
    private AlertDialog mDialog;
    private View mTaskFormView;
    private enum State {
        add_new,
        edit
    }
    private State st;

    void inflateTaskForm(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Run run = Run.getRunObject();

        View t = run.getActivity(R.layout.activity_main).getLayoutInflater().inflate(R.layout.task_form, null);
        run.addLayout(R.layout.task_form, t);
        builder.setView(t);
        mTaskFormView = t;

        ClickRegistration register = run.getRegister();
        register.registerResource(R.layout.task_form, R.id.taskOk, (parent, view, position, id) -> taskFormOk(parent, view, position, id));
        register.registerResource(R.layout.task_form, R.id.taskCancel, (parent, view, position, id) -> taskFormCancel(parent, view, position, id));

        mDialog = builder.create();
    }

    void newTaskForm() {
        st = State.add_new;
        Task.writeToForm(null, mTaskFormView);
        mDialog.show();
    }

    void editTaskForm(Task task) {
        st = State.edit;
        Task.writeToForm(task, mTaskFormView);
        mDialog.show();
    }

    void taskFormOk(AdapterView<?> parent, View view, int position, long id) {
        TaskAdapter taskAdapter = Run.getRunObject().getTaskAdapter();

        switch(st) {
            case add_new: {
                Task task = Task.readFromForm(view, null);
                taskAdapter.add(task);
                break;
            }

            case edit: {
                Task.readFromForm(view, taskAdapter.getItemClicked()).updateTaskFace();
                taskAdapter.notifyDataSetChanged();
                break;
            }
        }

        mDialog.dismiss();
    }

    void taskFormCancel(AdapterView<?> parent, View view, int position, long id) {
        mDialog.dismiss();
    }
}

class Task {
    //Task properties
    private String name;
    private View taskFace;
    private final Run mRun = Run.getRunObject();

    final private int DEFAULT_TASK_BACKGROUND = Run.getRunObject().getContext().getResources().getColor(R.color.defaultTaskFaceBackground);
    final private int COLOR_TASK_SELECTED = Run.getRunObject().getContext().getResources().getColor(R.color.taskFaceSelected);

    Task(String n) {
        name = n;
        taskFace = null;
    }

    //Operations on a Task
    String getName() {
        return name;
    }

    View getTaskFace() {
        if(taskFace != null)
            return taskFace;

        View v = LayoutInflater.from(mRun.getContext()).inflate(R.layout.task_face, null);

        TextView t = v.findViewById(R.id.task_face);
        t.setText(name);
        taskFace = v;
        return taskFace;
    }

    void updateTaskFace() {
        if(taskFace != null) {
            ((TextView) taskFace).setText(name);
        }
    }

    void select() {
        taskFace.setBackgroundColor(COLOR_TASK_SELECTED);
    }

    void removeSelection() {
        taskFace.setBackgroundColor(DEFAULT_TASK_BACKGROUND);
    }

    static Task readFromFile(BufferedReader readBuf) {
        try {
            String str = readBuf.readLine();
            if(str != null)
                return new Task(str);
        }
        catch(Exception e) {
            TBLog.e(e.toString());
        }
        return null;
    }

    static Task readFromForm(View v, Task task) {
        Run run = Run.getRunObject();
        EditText name = (EditText) run.findViewByResource(R.layout.task_form, R.id.taskName);
        if(task == null)
            task = new Task("");
        task.name = name.getText().toString();
        return task;
    }

    static void writeToFile(Task t, BufferedWriter writeBuf) {
        try {
            writeBuf.write(t.name, 0, t.name.length());
            writeBuf.write('\n');
        }
        catch (Exception e) {
            TBLog.e(e.toString());
        }
    }

    static void writeToForm(Task t, View v) {
        EditText name = v.findViewById(R.id.taskName);

        if(t == null) {
            name.setText("");
            return;
        }

        name.setText(t.name);
    }
}