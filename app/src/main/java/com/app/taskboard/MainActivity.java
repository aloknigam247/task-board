package com.app.taskboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Run mRun;
    private TaskList mTaskList;
    private final Actions mActions = new Actions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        mRun = Run.getRunObject();
        mRun.addActivity(R.layout.activity_main, this);
        mRun.setContext(this);
        mRun.startUp(this);
        mTaskList = mRun.getTaskList();
        //mTaskList.fillView(this); // TODO: removing for now
        inflateLayouts();
        registerActions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Start up code
    }

    @Override
    protected void onResume() {
        super.onResume();
        // resume code
    }

    @Override
    protected void onPause() {
        super.onPause();
        // pause code
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop code
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // destroy code
        mRun.shutDown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActions.itemClicked) {
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    mTaskList.remove(mActions.itemSelected);
                    break;
                }
                case R.id.action_move_up: {
                    mActions.itemSelected = mTaskList.moveUp(mActions.itemSelected);
                    break;
                }
                case R.id.action_move_down: {
                    mActions.itemSelected = mTaskList.moveDown(mActions.itemSelected);
                    break;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserPermission.onRequestResult(requestCode, permissions, grantResults);
    }

    void inflateLayouts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Run.getActivity(R.layout.activity_main));
        View t = Run.getActivity(R.layout.activity_main).getLayoutInflater().inflate(R.layout.activity_task_form, null);
        builder.setView(t);
        mActions.dialog = builder.create();
        mRun.addLayout(R.layout.activity_task_form, t);
    }

    void registerActions() {
        ActivityRegistration register = mRun.getRegister();
        register.registerResource(R.layout.activity_main, R.id.addTask, mActions, mTaskList, Actions.ADD_TASK);
        register.registerResource(R.layout.activity_main, R.id.taskDisplay, mActions, Actions.ON_ITEM_CLICK);
        register.registerResource(R.layout.activity_task_form, R.id.taskOk, mActions, mTaskList, Actions.TASK_OK);
        register.registerResource(R.layout.activity_task_form, R.id.taskCancel, mActions, mTaskList, Actions.TASK_CANCEL);
    }

    class Actions implements ActivityRegistration.RegisterClick {
        final static int ADD_TASK       = 0;
        final static int ON_ITEM_CLICK  = 1;
        final static int TASK_OK        = 2;
        final static int TASK_CANCEL    = 3;
        AlertDialog dialog;
        boolean itemClicked = false;
        int itemSelected;

        public void onButtonClick(View v, List<Object> argArray) {
            TaskList mTaskList = (TaskList) argArray.get(0);
            switch ((int) argArray.get(1)) {
                case ADD_TASK: {
                    dialog.show();
                    break;
                }
                case TASK_OK: {
                    EditText newTask = (EditText)Run.findViewByResource(R.layout.activity_task_form, R.id.newTask);
                    mTaskList.add(new Task(newTask.getText().toString()));
                    newTask.setText("");
                    dialog.cancel();
                    break;
                }
                case TASK_CANCEL: {
                    EditText newTask = (EditText)Run.findViewByResource(R.layout.activity_task_form, R.id.newTask);
                    newTask.setText("");
                    dialog.dismiss();
                    break;
                }
            }
        }

        public void onAdapterClick (AdapterView<?> parent, View view, int position, long id, List<Object> argArray) {
            switch((int) argArray.get(0)) {
                case ON_ITEM_CLICK: {
                    itemClicked = true;
                    itemSelected = position;
                }
            }
        }
    }
}