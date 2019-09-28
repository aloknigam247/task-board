package com.app.taskboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.lang.Override;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    private Run mRun;
    private TaskAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mRun = Run.getRunObject();
        mRun.setActivity(R.layout.activity_main, this, this);
        mRun.setContext(this);
        mRun.startUp(this);
        mTaskAdapter = mRun.getTaskAdapter();

        AdapterView taskView = (AdapterView) mRun.findViewByResource(R.layout.activity_main, R.id.taskDisplay);
        if(taskView != null) {
            taskView.setAdapter(mTaskAdapter);
        }
        else {
            //TODO: add case later
        }

        mRun.getRegister().registerResource(
                R.layout.activity_main,
                R.id.addTask,
                (AdapterView<?> parent, View view, int position, long id) -> {
                    mRun.getTaskAdapter().showNewForm();
                }
        );
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
        if(mTaskAdapter.isItemClicked()) {
            switch (item.getItemId()) {
                case R.id.action_edit: {
                    mTaskAdapter.editClickedTask();
                    break;
                }
                case R.id.action_delete: {
                    mTaskAdapter.remove(mTaskAdapter.getItemClickedId());
                    break;
                }
                case R.id.action_move_up: {
                    mTaskAdapter.moveUp(mTaskAdapter.getItemClickedId());
                    break;
                }
                case R.id.action_move_down: {
                    mTaskAdapter.moveDown(mTaskAdapter.getItemClickedId());
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
}