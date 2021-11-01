package com.run.ultimate_fitness.water;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Water_Tracker_DOBHelper extends SQLiteOpenHelper {
    public static final String WATER_TABLE = "WATER_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_CUPS_OF_WATER = "CUPS_OF_WATERC";
    //private static final String COLUMN_SIZE_OF_CUP = "SIZE_OF_CUPC";
    public static final String COLUMN_ID = "ID";

    public Water_Tracker_DOBHelper(@Nullable Context context ) {
        super(context, "WATER_UF.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE " + WATER_TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, " + COLUMN_CUPS_OF_WATER + " INTEGER)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + WATER_TABLE);
    }

    public Boolean addWaterProgress(WaterModel waterModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUPS_OF_WATER, waterModel.getCups_of_waterC());
        cv.put(COLUMN_DATE, waterModel.getDate());
        //cv.put(COLUMN_SIZE_OF_CUP, waterModel.getSize_of_cup());

        long insert = db.insert(WATER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + WATER_TABLE ;

        Cursor cursor = db.rawQuery(queryString, null);


        if (cursor.moveToNext()){
            return true;
        }else {
            return false;
        }
    }

    public boolean deleteOne(WaterModel waterModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + WATER_TABLE + " WHERE " + COLUMN_ID + " = " + waterModel.getId() ;

        Cursor cursor = db.rawQuery(queryString, null);


        if (cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }


    public boolean updateData(WaterModel waterModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUPS_OF_WATER, waterModel.getCups_of_waterC());
        cv.put(COLUMN_DATE, waterModel.getDate());
        //cv.put(COLUMN_SIZE_OF_CUP, waterModel.getSize_of_cup());

        long insert = db.update(WATER_TABLE, cv, "id=?", new String[] {COLUMN_ID});
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public List<WaterModel> getInfo() {
        List<WaterModel> returnList = new ArrayList<>();
        //Getting Data
        String queryString = "SELECT * FROM " + WATER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);


        if(cursor.moveToFirst()){
            // if result true move
            do{
                int waterID = cursor.getInt(0);
                String waterDate = cursor.getString(1);
                int waterCups_of_water = cursor.getInt(2);
                //int waterSize_of_cups = cursor.getInt(3);

                WaterModel newWaterEntry =
                        new WaterModel(waterID, waterDate, waterCups_of_water);
                returnList.add(newWaterEntry);

            } while (cursor.moveToNext());
        }else {
            //do something
        }

        cursor.close();
        db.close();
        return returnList;
    }



}


