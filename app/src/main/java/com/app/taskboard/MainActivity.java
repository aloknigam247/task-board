package com.app.taskboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Run mRun;
    private TaskList mTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRun = new Run(R.layout.activity_main, this, this);
        mRun.startUp();
        mTaskList = mRun.getTaskList();
        //mTaskList.fillView(this); // TODO: removing for now
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserPermission.onRequestResult(requestCode, permissions, grantResults);
    }

    void registerActions() {
        ActivityRegistration register = new ActivityRegistration();
        register.registerResource(R.layout.activity_main, R.id.addTask, new Actions(), mTaskList, Actions.ADD_TASK);
        register.registerResource(R.layout.activity_main, R.id.deleteTask, new Actions(), mTaskList, Actions.DELETE_TASK);
        register.registerResource(R.layout.activity_main, R.id.taskDisplay, new Actions(), Actions.ONCLICK);
        register.registerResource(R.layout.activity_main, R.id.moveUp, new Actions(), mTaskList, Actions.UP);
        register.registerResource(R.layout.activity_main, R.id.moveDown, new Actions(), mTaskList, Actions.DOWN);
    }
}

class Actions implements ActivityRegistration.RegisterClick {
    final static int ADD_TASK    = 0;
    final static int DELETE_TASK = 1;
    final static int ONCLICK     = 2;
    final static int UP          = 3;
    final static int DOWN        = 4;
    static boolean itemClicked = false;
    static int itemSelected;

    public void onButtonClick(View v, List<Object> argArray) {
        TaskList mTaskList = (TaskList) argArray.get(0);
        switch ((int) argArray.get(1)) {
            case ADD_TASK: {
                EditText newTask = (EditText)Run.findViewByActivity(R.layout.activity_main, R.id.newTask);
                mTaskList.add(new Task(newTask.getText().toString()));
                newTask.setText("");
                break;
            }
            case DELETE_TASK: {
                mTaskList.remove(itemSelected);
                break;
            }
            case UP: {
                if(itemClicked) {
                    itemSelected = mTaskList.moveUp(itemSelected);
                }
                break;
            }
            case DOWN: {
                if(itemClicked) {
                    itemSelected = mTaskList.moveDown(itemSelected);
                }
                break;
            }
        }
    }

    public void onAdapterClick (AdapterView<?> parent, View view, int position, long id, List<Object> argArray) {
        switch((int) argArray.get(0)) {
            case ONCLICK: {
                itemClicked = true;
                itemSelected = position;
            }
        }
    }

}
