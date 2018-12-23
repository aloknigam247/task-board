package com.app.taskboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Actions.itemClicked) {
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    mTaskList.remove(Actions.itemSelected);
                    break;
                }
                case R.id.action_move_up: {
                    Actions.itemSelected = mTaskList.moveUp(Actions.itemSelected);
                    break;
                }
                case R.id.action_move_down: {
                    Actions.itemSelected = mTaskList.moveDown(Actions.itemSelected);
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

    void registerActions() {
        ActivityRegistration register = new ActivityRegistration();
        register.registerResource(R.layout.activity_main, R.id.addTask, new Actions(), mTaskList, Actions.ADD_TASK);
        register.registerResource(R.layout.activity_main, R.id.taskDisplay, new Actions(), Actions.ONCLICK);
    }
}

class Actions implements ActivityRegistration.RegisterClick {
    final static int ADD_TASK    = 0;
    final static int ONCLICK     = 2;
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
