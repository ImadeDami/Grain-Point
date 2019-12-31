package com.zeathon.grainpoint_agent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.zeathon.grainpoint_agent.DataBaseHelper.TABLE_NAME;

public class DataBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DataBHelper";
    public static final String TABLE_NAME = "newrecord2";
    public static final String COL1 = "id";
    public static final String COL2 = "FULLNAM";
    public static final String COL3 = "PHONENUM";
    public static final String COL4 = "CROPTYPE";
    public static final String COL5 = "WEIGHT";
    public static final String COL6 = "MOISTURECON";
    public static final String COL7 = "DATE";
    public static final String SYNC_STATUS = "syncstatus";

    public DataBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FULLNAM TEXT, PHONENUM TEXT, CROPTYPE TEXT, WEIGHT TEXT, MOISTURECON TEXT, DATE TEXT,  syncstatus integer)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String fName, String phoneNm, String crpTyp, String weig, String moistCn, String dat, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, fName);
        contentValues.put(COL3, phoneNm);
        contentValues.put(COL4, crpTyp);
        contentValues.put(COL5, weig);
        contentValues.put(COL6, moistCn);
        contentValues.put(COL7, dat);
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
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
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


}


