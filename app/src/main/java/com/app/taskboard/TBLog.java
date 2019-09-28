package com.app.taskboard;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TBLog {

    static void c(String msg) {

    }

    static void d(String msg) {
        if(BuildConfig.DEBUG)
            Log.d("tb-debug", msg);
    }

    static void e(String msg) {
        Intent intent = new Intent(Run.getRunObject().getContext(), ActivityLogWindow.class);
        intent.putExtra("Report", msg);
        Run.getRunObject().getContext().startActivity(intent);
    }

    static void t(String msg) {
        Toast.makeText(Run.getRunObject().getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
