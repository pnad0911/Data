package com.example.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataControl {

    private SQLiteDatabase write, read;
    private MySQLite mySQLite;
    private Cursor cursor;


    public static String strNull = "";

    class Proxy {
        public Cursor c;
        public boolean exist;

        public Proxy(Cursor c, boolean exist) {
            this.c = c;
            this.exist = exist;
        }
    }

    DataControl(Context context) {
        mySQLite = new MySQLite(context);
        write = mySQLite.getWritableDatabase();
        read = mySQLite.getReadableDatabase();
    }

    private boolean checkValidInput(DataContract.DataObject dataObject) {
        if(dataObject.id.length() == 0 || dataObject.email.length() == 0 ||
                dataObject.reg == null || dataObject.server.length() == 0)
            return false;
        return true;
    }


    public Boolean addTuple(DataContract.DataObject dataObject) {
        if(!checkValidInput(dataObject)) return null;
        ContentValues values = new ContentValues();
        values.put(DataContract.Entry.COLUMN_DEVICE,dataObject.id);
        values.put(DataContract.Entry.COLUMN_EMAIL, dataObject.email);
        values.put(DataContract.Entry.COLUMN_IS_REG, dataObject.reg ? 1 : 0);
        values.put(DataContract.Entry.COLUMN_SERVER, dataObject.server);

        if(!existTup(dataObject.id).exist) {
            if(write.insert(DataContract.Entry.TABLE_NAME, null, values) == -1)
                return false;
            else return true;
        }
        else return false;
    }

    private class pair {
        String query;
        String[] c;
        pair(String query, String[] c) {
            this.query = query;
            this.c = c;
        }
    }

    private pair queryES(String email, String server) {
        if((email.length() > 0 && server.length() > 0) ||
                (email.length() == 0 && server.length() == 0))
            return new pair(String.format("SELECT * FROM %s WHERE %s LIKE %s and %s LIKE %s",
                    DataContract.Entry.TABLE_NAME, DataContract.Entry.COLUMN_EMAIL, "?",
                    DataContract.Entry.COLUMN_SERVER, "?"),new String[]{email,server});
        else if(email.length() > 0 && server.length() == 0)
            return new pair(String.format("SELECT * FROM %s WHERE %s LIKE %s",
                    DataContract.Entry.TABLE_NAME, DataContract.Entry.COLUMN_EMAIL, "?"),new String[]{email});
        else
            return new pair(String.format("SELECT * FROM %s WHERE %s LIKE %s",
                    DataContract.Entry.TABLE_NAME, DataContract.Entry.COLUMN_SERVER, "?"),new String[]{server});
    }

    public List<DataContract.DataObject> existES(String email, String server) {
        pair query = queryES(email,server);
        cursor = read.rawQuery(query.query,query.c);
        List<DataContract.DataObject> res = new ArrayList<>();
        while(cursor.moveToNext()) {
            String fid = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_DEVICE));
            String femail = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_EMAIL));
            boolean freg = cursor.getLong(cursor.getColumnIndexOrThrow(
                    DataContract.Entry.COLUMN_IS_REG)) == 1 ? true : false;
            String fserver = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_SERVER));
            res.add(new DataContract.DataObject(fid,femail,freg,fserver));
        }
        return res;
    }

    public boolean update(String newID, DataContract.DataObject old) {
        ContentValues values = new ContentValues();
        values.put(DataContract.Entry.COLUMN_DEVICE, newID);
        String selection = DataContract.Entry.COLUMN_DEVICE + " = ? and " +
                DataContract.Entry.COLUMN_EMAIL + " = ? and " +
                DataContract.Entry.COLUMN_SERVER + " = ?";
        String[] selectionArgs = { old.id,old.email,old.server };
        try {
            write.update(
                    DataContract.Entry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Proxy existTup(String ID) {
        String query = String.format("SELECT * FROM %s WHERE %s = %s",
                DataContract.Entry.TABLE_NAME, DataContract.Entry.COLUMN_DEVICE, "?");
        cursor = read.rawQuery(query,new String[]{ID});
        if(cursor.getCount() <= 0) {
//            cursor.close();
            return new Proxy(cursor,false);
        }
//        cursor.close();
        return new Proxy(null,true);
    }

    public void print() {
        cursor = read.query(
                DataContract.Entry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        while(cursor.moveToNext()) {
            String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_DEVICE));
            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_EMAIL));
            long reg = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_IS_REG));
            String server = cursor.getString(
                    cursor.getColumnIndexOrThrow(DataContract.Entry.COLUMN_SERVER));
            Log.println(Log.DEBUG, "Database", "Device ID: " + id + "; Email Address: " + email +
                    "; Is Registered: " + ((int)reg == 1 ? "True" : "False") +
                    "; Server Address: " + server);
        }
        cursor.close();
    }

    public void close() {
        mySQLite.close();
    }
}
