package com.app.taskboard;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

class ActivityRegistration {

    //Interface to implement Clicks
    interface RegisterClick {
        default void onButtonClick(View v, List<Object> argArray) {}
        default void onAdapterClick(AdapterView<?> parent, View view, int position, long id, List<Object> argArray) {}
    }

    void registerResource(int activityRes, int resource, final RegisterClick implementor, final Object ...args) {
        View view = Run.findViewByActivity(activityRes, resource);
        // set listeners for Button clicks
        if(view instanceof Button) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    implementor.onButtonClick(v, getArgArray(args));
                }
            });
        }

        else if(view instanceof AdapterView) {   // set listeners for Adapter clicks
            AdapterView adapterView = (AdapterView) view;
            adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    implementor.onAdapterClick(parent, view, position, id, getArgArray(args));
                }
            });
        }
    }

    List<Object> getArgArray(Object ...args) {
        List<Object> argArray= new ArrayList<>();
        for(Object obj : args)
            argArray.add(obj);
        return argArray;
    }
}