package com.gamecodeschool.garagepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * Created by FIRAOL DIDA on 08-Mar-18.
 */

public class MyDBHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "GarageNameList.db";
    private static final String TABLE_NAMELISTS = "namelists";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTACTNAME = "contactname";
    private static final String COLUMN_CONTACTPHONE = "contactphone";
    private static final String COLUMN_CONTACTPHONE2 = "contactphone2";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_FINISHDATE = "finishdate";
    private static final String COLUMN_FINISHMONTH= "finishmonth";
    private static final String COLUMN_KILOMETER= "kilometer";
    private static final String COLUMN_PLATENUMBER= "platenumber";
    private static final String COLUMN_FLAG= "flag";
    private static final String DATABASE_ALTER_CONTACTPHONE2 = "ALTER TABLE " + TABLE_NAMELISTS + " ADD " + COLUMN_CONTACTPHONE2 + " INTEGER;";

    public MyDBHandler(Context context){
        super( context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("MyDBhandler", "MyDBHandler: ");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("onCreate", "onCreate: ");
        String query = "CREATE TABLE " + TABLE_NAMELISTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACTNAME + " TEXT, " +
                COLUMN_CONTACTPHONE + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_FINISHDATE + " INTEGER, " +
                COLUMN_FINISHMONTH + " INTEGER, " +
                COLUMN_KILOMETER + " INTEGER, " +
                COLUMN_PLATENUMBER + " TEXT, " +
                COLUMN_FLAG + " INTEGER, " +
                COLUMN_CONTACTPHONE2 + " INTEGER " +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("TEST", "onUpgrade: working");
        if (newVersion > oldVersion) {
            Log.i("IN IF", "onUpgrade: IF CASE");
            sqLiteDatabase.execSQL(DATABASE_ALTER_CONTACTPHONE2);
            Log.i("AFTER ALTER", "onUpgrade: ALTER");
        }
    }

    public boolean addData(String name, int phone, String desc, int finish, int f_month, int km, String plateNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CONTACTNAME, name);
        contentValues.put(COLUMN_CONTACTPHONE, phone);
        contentValues.put(COLUMN_DESCRIPTION, desc);
        contentValues.put(COLUMN_FINISHDATE, finish);
        contentValues.put(COLUMN_FINISHMONTH, f_month);
        contentValues.put(COLUMN_KILOMETER, km);
        contentValues.put(COLUMN_PLATENUMBER, plateNumber);
        contentValues.put(COLUMN_FLAG, 1);

        long result = sqLiteDatabase.insert(TABLE_NAMELISTS, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addData(String name, int phone, int phone2, String desc, int finish, int f_month, int km, String plateNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CONTACTNAME, name);
        contentValues.put(COLUMN_CONTACTPHONE, phone);
        contentValues.put(COLUMN_DESCRIPTION, desc);
        contentValues.put(COLUMN_FINISHDATE, finish);
        contentValues.put(COLUMN_FINISHMONTH, f_month);
        contentValues.put(COLUMN_KILOMETER, km);
        contentValues.put(COLUMN_PLATENUMBER, plateNumber);
        contentValues.put(COLUMN_FLAG, 1);
        contentValues.put(COLUMN_CONTACTPHONE2, phone2);

        long result = sqLiteDatabase.insert(TABLE_NAMELISTS, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }


    public void deleteName(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" );
    }

    public Cursor getId(String name, int phone){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_CONTACTNAME + "=\"" + name + "\"" +
                " AND " + COLUMN_CONTACTPHONE + "=\"" + phone + "\";", null);

        return cursor;
    }


    public Cursor getAllRows(){
        int num = 1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_FLAG + "=\"" + num + "\";", null);

        return cursor;
    }

    public Cursor getPhone(int time, int todays_month) {
        int flag = 1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_FINISHDATE + "=\"" + time + "\"" +
                " AND " + COLUMN_FLAG + "=\"" + flag + "\"" + " AND " + COLUMN_FINISHMONTH + "=\"" + todays_month + "\";", null);

        return cursor;
    }

    public Cursor getFlagInfo(){
        int num = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_FLAG + "=\"" + num + "\";", null);

        return cursor;
    }

    public void updateFlag(String name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FLAG, 0);
        sqLiteDatabase.update(TABLE_NAMELISTS, contentValues, COLUMN_CONTACTNAME + "=\"" + name + "\"" , null);
    }

    public void updateDate(int id, int day, int month){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FINISHDATE, day);
        contentValues.put(COLUMN_FINISHMONTH, month);
        contentValues.put(COLUMN_FLAG, 1);
        sqLiteDatabase.update(TABLE_NAMELISTS, contentValues, COLUMN_ID + "=\"" + id + "\"", null);
    }

    public void updateKilometer(int id, int km){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_KILOMETER, km);
        sqLiteDatabase.update(TABLE_NAMELISTS, contentValues, COLUMN_ID + "=\"" + id + "\"", null);
    }

    public void updateDesc(int id, int km, String plateNumber, String name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String desc = "Dear " + name + ", it's your car service time. Plate number: " + plateNumber + ", Kilometer: " + km + ".\nThank you, Daniel Garage";
        contentValues.put(COLUMN_DESCRIPTION, desc);
        sqLiteDatabase.update(TABLE_NAMELISTS, contentValues, COLUMN_ID + "=\"" + id + "\"", null);
    }

    public boolean checkForExistance(String name, int phone, int phone2){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_CONTACTNAME + "=\"" + name + "\"" +
                " AND " + COLUMN_CONTACTPHONE + "=\"" + phone + "\"" + " AND " + COLUMN_CONTACTPHONE2 + "=\"" + phone2 + "\";", null);
        if (cursor.getCount() <= 0){
            return false;
        }
        return true;
    }
    public boolean checkForExistance(String name, int phone){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMELISTS + " WHERE " + COLUMN_CONTACTNAME + "=\"" + name + "\"" +
                " AND " + COLUMN_CONTACTPHONE + "=\"" + phone + "\";", null);
        if (cursor.getCount() <= 0){
            return false;
        }
        return true;
    }
}
