package com.gamecodeschool.garagepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by FIRAOL DIDA on 08-Mar-18.
 */

public class MyDBHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GarageNameList.db";
    private static final String TABLE_NAMELISTS = "namelists";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTACTNAME = "contactname";
    private static final String COLUMN_CONTACTPHONE = "contactphone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_FINISHDATE = "finishdate";
    private static final String COLUMN_FINISHMONTH= "finishmonth";
    private static final String COLUMN_KILOMETER= "kilometer";
    private static final String COLUMN_PLATENUMBER= "platenumber";
    private static final String COLUMN_FLAG= "flag";

    public MyDBHandler(Context context){
        super( context, DATABASE_NAME, null, 1);
    }

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAMELISTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACTNAME + " TEXT, " +
                COLUMN_CONTACTPHONE + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_FINISHDATE + " INTEGER, " +
                COLUMN_FINISHMONTH + " INTEGER, " +
                COLUMN_KILOMETER + " INTEGER, " +
                COLUMN_PLATENUMBER + " TEXT, " +
                COLUMN_FLAG + " INTEGER " +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMELISTS);
        onCreate(sqLiteDatabase);
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
