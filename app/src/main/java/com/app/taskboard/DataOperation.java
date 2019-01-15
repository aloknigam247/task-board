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
    private TaskAdapter mTaskAdapter;

    boolean connect() {
        dataBase = null;
        File db = new File(absFilePath);
        try {
            if(!db.exists()) {
                db.createNewFile(); //TODO: check return value
                db.setReadable(true);   //TODO: check return value
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
        mTaskAdapter = null;
    }

    TaskAdapter load(Context context) {
        mTaskAdapter = new TaskAdapter(context);
        try (FileReader dbReader = new FileReader(dataBase)) {
            BufferedReader dbBuf = new BufferedReader(dbReader);
            Task temp;
            // file is already sorted, only load here for faster startup
            while ( (temp = Task.readFromFile(dbBuf)) != null)
                mTaskAdapter.addFromFile(temp);

            dbBuf.close();

        } catch (Exception e) {
            //TODO: add case later
        }

        // create a backup TODO: remove code later when not in use
        if(mTaskAdapter == null || mTaskAdapter.getCount() == 0)  //overwrite backup only if database read is success
            return mTaskAdapter;
        File backUp = new File(absFilePath + ".bk");
        backUp.delete();    //TODO: check return value
        try(FileOutputStream fout = new FileOutputStream(absFilePath + ".bk");
            FileInputStream  fin  = new FileInputStream(absFilePath) ) {
            int i;
            while ( (i = fin.read()) > 0 )
                fout.write(i);
        } catch(Exception e) {
            //TODO: add case later
        }

        return mTaskAdapter;
    }

    void save() {
        //TODO: update database only if database is changed
        clearDBFile();
        try(FileWriter dbWriter = new FileWriter(dataBase)) {
            BufferedWriter dbBuf = new BufferedWriter(dbWriter);
            for(int i = 0; i< mTaskAdapter.getCount(); i++)
                Task.writeToFile((Task) mTaskAdapter.getItem(i), dbBuf);
            dbBuf.close();
        } catch(Exception e) {
            //TODO: add case later
        }
    }

    private void clearDBFile(){
        if(dataBase == null) {
            //TODO: add case later
        }
        dataBase.delete();  //TODO: check return value
        connect();  //TODO: infer return value
    }
}
