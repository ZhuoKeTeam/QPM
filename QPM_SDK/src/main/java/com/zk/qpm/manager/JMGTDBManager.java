package com.jm.android.gt.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class JMGTDBManager {

    private static volatile JMGTDBManager instance;

    private JMGTDBManager() {
    }

    public static JMGTDBManager getInstance() {
        if (instance == null) {
            synchronized (JMGTDBManager.class) {
                if (instance == null) {
                    instance = new JMGTDBManager();
                }
            }
        }
        return instance;
    }

    public File[] getAllDBFiles(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir == null || filesDir.getParentFile() == null) {
            return new File[]{};
        }
        File dataDir = filesDir.getParentFile();
        File[] dbDirs = dataDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return TextUtils.equals(name, "databases");
            }
        });
        if (dbDirs.length == 0) {
            return new File[]{};
        }
        File[] dbFiles = dbDirs[0].listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !TextUtils.isEmpty(name) && !name.endsWith("-journal");
            }
        });
        return dbFiles;
    }

    public SQLiteDatabase openDB(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            return SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllTableName(SQLiteDatabase db) {
        List<String> result = new ArrayList<>();
        if (db == null) {
            return result;
        }
        Cursor cursor = null;
        try {
            String sql = "select name from sqlite_master where type = 'table' order by name";
            cursor = db.rawQuery(sql, null);
            if (cursor == null) {
                return result;
            }
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                result.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<String> getAllFieldName(SQLiteDatabase db, String tableName){
        List<String> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "PRAGMA table_info([" + tableName + "])";
            cursor = db.rawQuery(sql, null);
            if (cursor == null) {
                return result;
            }
            while (cursor.moveToNext()){
                result.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}
