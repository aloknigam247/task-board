package com.app.taskboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class ActivityLogWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_window);

        Run mRun = Run.getRunObject();
        mRun.setActivity(R.layout.activity_log_window, this, this);

        mRun.getRegister().registerResource(
                R.layout.activity_log_window,
                R.id.abort_ok,
                (AdapterView<?> parent, View view, int position, long id) -> {
                    System.exit(0);
                }
        );

        TextView logWindow = findViewById(R.id.log_window);
        Intent intent = getIntent();

        logWindow.setText(intent.getStringExtra("Report"));
    }
}
