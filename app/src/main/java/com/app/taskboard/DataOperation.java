package com.app.taskboard;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

class DataOperation {
    private final String rootDir = Environment.getExternalStorageDirectory().getPath();
    private final String dbFile = "tasks.txt";
    private final String absFilePath = rootDir + "/" + dbFile;
    private static File dataBase;
    private TaskList mTaskList;

    boolean connect() {
        dataBase = null;
        File db = new File(absFilePath);
        try {
            if(!db.exists()) {
                db.createNewFile();
                db.setReadable(true);
            }
        }
        catch (Exception e){
            //TODO: add process later
            return false;
        }
        dataBase = db;
        return true;
    }

    void disconnect() {
        dataBase = null;
        mTaskList = null;
    }

    TaskList load(Context context) {
        // create a backup before loading TODO: remove code later when not in use
        File backUp = new File(absFilePath + ".bk");
        backUp.delete();
        try(FileOutputStream fout = new FileOutputStream(absFilePath + ".bk");
            FileInputStream  fin  = new FileInputStream(absFilePath) ) {
            int i;
            while ( (i = fin.read()) > 0 )
                fout.write(i);
        } catch(Exception e) {
            //TODO: add case later
        }

        mTaskList = new TaskList(context);
        try (FileReader dbReader = new FileReader(dataBase)) {
            BufferedReader dbBuf = new BufferedReader(dbReader);
            Task temp;
            // file is already sorted, only load here for faster startup
            while ( (temp = Task.readTask(dbBuf)) != null)
                mTaskList.add(temp);

            dbBuf.close();

        } catch (Exception e) {
            //TODO: add case later
        }
        return mTaskList;
    }

    void save() {
        //TODO: update database only if database is changed
        clearDBFile();
        try(FileWriter dbWriter = new FileWriter(dataBase)) {
            BufferedWriter dbBuf = new BufferedWriter(dbWriter);
            for(int i=0; i<mTaskList.size(); i++)
                Task.writeTask(mTaskList.getTask(i), dbBuf);
            dbBuf.close();
        } catch(Exception e) {
            //TODO: add case later
        }
    }

    private void clearDBFile(){
        if(dataBase == null) {
            //TODO: add case later
        }
        dataBase.delete();
        connect();  //TODO: infer return value
    }
}
