package com.zeathon.grainpoint_agent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DataBaseHelper";
    public static final String TABLE_NAME = "newfarmer4";
    public static final String COL1 = "id";
    public static final String COL18 = "USERID";
    public static final String COL2 = "FULLNAM";
    public static final String COL3 = "PHONENUM";
    public static final String COL4 = "IDTYPE";
    public static final String COL5 = "IDNUM";
    public static final String COL6 = "BANKNAM";
    public static final String COL7 = "BANKACC";
    public static final String COL8 = "BANKVNUM";
    public static final String COL9 = "FARMSIZ";
    public static final String COL10 = "PICTURE";
    public static final String COL11 = "GPSLOC";
    public static final String COL12 = "GENDER";
    public static final String COL13 = "STATE";
    public static final String COL14 = "LGA";
    public static final String COL15 = "COOPGRP";
    public static final String COL16 = "ROLEGRP";
    public static final String COL17 = "CROP";

    public static final String SYNC_STATUS = "syncstatus";

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT, FULLNAM TEXT, PHONENUM TEXT, IDTYPE TEXT, IDNUM TEXT, BANKNAM TEXT, BANKACC TEXT, BANKVNUM TEXT, FARMSIZ TEXT, PICTURE TEXT, GPSLOC TEXT, GENDER TEXT, " +
                "STATE TEXT, LGA TEXT, COOPGRP TEXT, ROLEGRP TEXT, CROP TEXT, syncstatus integer)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String userID, String fName, String phoneNm, String idTy, String idNumb, String bankNam, String bankAc, String bankVN, String farmSz, String Image, String gpsLoc,
    String gend, String sta, String localG, String grpCoop, String grpRol, String crop, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL18, userID);
        contentValues.put(COL2, fName);
        contentValues.put(COL3, phoneNm);
        contentValues.put(COL4, idTy);
        contentValues.put(COL5, idNumb);
        contentValues.put(COL6, bankNam);
        contentValues.put(COL7, bankAc);
        contentValues.put(COL8, bankVN);
        contentValues.put(COL9, farmSz);
        contentValues.put(COL10, Image);
        contentValues.put(COL11, gpsLoc);
        contentValues.put(COL12, gend);
        contentValues.put(COL13, sta);
        contentValues.put(COL14, localG);
        contentValues.put(COL15, grpCoop);
        contentValues.put(COL16, grpRol);
        contentValues.put(COL17, crop);
        contentValues.put(SYNC_STATUS, sync_status);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted correctly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

        public Cursor getData() {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME;
            Cursor data = db.rawQuery(query, null);

            return data;
        }

    public boolean updateNameStatus(int id, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, sync_status);
        db.update(TABLE_NAME, contentValues, COL1 + "=" + id, null);
        db.close();
        return true;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public List<String> getFN() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getPN() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<String> getAllState() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
       // String selectQuery = "SELECT * FROM " + TABLE_NAME + "ORDER BY" + "STATE" + "ASC" + " LIMIT 1";
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(13));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getLG() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(14));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getGrpCop() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(15));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getGrpRole() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(16));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getBank() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getCrps() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
       // String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(17));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
}