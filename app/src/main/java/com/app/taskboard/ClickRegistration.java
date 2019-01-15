package com.app.taskboard;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

class ClickRegistration {

    //Interface to implement Clicks
    interface RegisterClick {
        void execute(AdapterView<?> parent, View view, int position, long id);
    }

    void registerResource(int activityRes, int resource, RegisterClick implementor) {
        View view = Run.getRunObject().findViewByResource(activityRes, resource);

        // set listeners for Button clicks
        if (view instanceof Button || view instanceof FloatingActionButton) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    implementor.execute(null, v, -1, -1);
                }
            });
        }

        // set listeners for Adapter clicks
        else if (view instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) view;
            adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    implementor.execute(parent, view, position, id);
                }
            });
        }
    }
}