package com.example.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataControl {
    private SQLiteDatabase write, read;
    private MySQLite mySQLite;
    private Cursor cursor;

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


    public boolean addTuple(String id, String email, String reg, String server) {
        ContentValues values = new ContentValues();
        values.put(DataContract.Entry.COLUMN_DEVICE,id);
        values.put(DataContract.Entry.COLUMN_EMAIL, email);
        values.put(DataContract.Entry.COLUMN_IS_REG, reg.equals("True") ? 1 : 0);
        values.put(DataContract.Entry.COLUMN_SERVER, server);

        if(!existTup(id).exist) {
            if(write.insert(DataContract.Entry.TABLE_NAME, null, values) == -1)
                return false;
            else return true;
        }
        else return false;
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
            System.out.println("Device ID: " + id + "; Email Address: " + email +
                    "; Is Registered: " + ((int)reg == 1 ? "True" : "False") +
                    "; Server Address: " + server);
        }
        cursor.close();
    }

    public void close() {
        mySQLite.close();
    }
}
