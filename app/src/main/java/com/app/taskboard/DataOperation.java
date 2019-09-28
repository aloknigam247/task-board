package com.app.taskboard;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class DataOperation {
    private final String rootDir = Environment.getExternalStorageDirectory().getPath();
    private final String dbFile = "tasks.txt";
    private final String absFilePath = rootDir + "/" + dbFile;
    private static File dataBase;
    private TaskAdapter mTaskAdapter;

    boolean connect() {
        dataBase = null;
        File db = new File(absFilePath);
        try {
            if (!db.exists()) {
                db.createNewFile();
                if (!db.setReadable(true))
                    TBLog.e(absFilePath + "can not set readable");
            }
        } catch (Exception e) {
            TBLog.e(e.toString());
            return false;
        }
        dataBase = db;
        return true;
    }

    void disconnect() {
        dataBase = null;
        mTaskAdapter = null;
    }

    TaskAdapter load(Context context) {
        mTaskAdapter = new TaskAdapter(context);
        try (FileReader dbReader = new FileReader(dataBase)) {
            BufferedReader dbBuf = new BufferedReader(dbReader);
            Task temp;

            while ((temp = Task.readFromFile(dbBuf)) != null)
                mTaskAdapter.addFromFile(temp);

            dbBuf.close();

        } catch (Exception e) {
            TBLog.e(e.toString());
        }

        return mTaskAdapter;
    }

    void save() {
        //TODO: edit database only if database is changed
        try (FileWriter dbWriter = new FileWriter(dataBase)) {
            BufferedWriter dbBuf = new BufferedWriter(dbWriter);
            for (int i = 0; i < mTaskAdapter.getCount(); i++)
                Task.writeToFile((Task) mTaskAdapter.getItem(i), dbBuf);
            dbBuf.close();
        } catch (Exception e) {
            TBLog.e(e.toString());
        }
    }
}