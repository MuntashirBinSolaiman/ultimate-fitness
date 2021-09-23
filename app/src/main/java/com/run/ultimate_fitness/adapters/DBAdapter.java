package com.run.ultimate_fitness.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    /*Variables----------------------------------*/
    private static final String databaseName = "stramdiet";
    private static final int databaseVersion = 11;

    /*Database Variables----------------------------------*/
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /*Class Adapter----------------------------------*/
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);

    }

    /*Database Helper----------------------------------*/
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                //CREATE TABLES
                //categories table

                //food diary calorie eaten table
                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary_cal_eaten (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "cal_eaten_id INTEGER," +
                        " cal_eaten_date DATE," +
                        "cal_eaten_meal_no INT," +
                        "cal_eaten_energy INT," +
                        "cal_eaten_proteins INT," +
                        "cal_eaten_carbs INT," +
                        "cal_eaten_fat INT) ; ");

            }
            catch (SQLException e){
                e.printStackTrace();
            }
            try{

                //create tables
                //food items consumed by user table
                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary (" +
                       " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "fd_id INTEGER,"+
                        "fd_date DATE, " +
                        "fd_meal_number INT," +
                        "fd_food_id INT," +
                        "fd_serving_size DOUBLE," +
                        "fd_serving_measurement VARCHAR,"+
                        "fd_energy_calculated DOUBLE," +
                        "fd_proteins_calculated DOUBLE," +
                        "fd_carbohydrates_calculated DOUBLE," +
                        "fd_fat_calculated DOUBLE," +
                        "fd_fat_meal_id INT) ; ");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            try {


                //categories tables
                db.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "category_id INTEGER," +
                        "category_name VARCHAR," +
                        "category_parent_id INT," +
                        "category_icon VARCHAR," +
                        "category_note VARCHAR) ;");

            }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                try{
                //create tables
                //main food table
                db.execSQL("CREATE TABLE IF NOT EXISTS food (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "food_id INTEGER,"+
                        "food_name VARCHAR, " +
                        "food_manufactor_name VARCHAR," +
                        "food_description VARCHAR," +
                        "food_serving_size DOUBLE," +
                        "food_serving_measurement VARCHAR," +
                        "food_serving_name_number DOUBLE," +
                        "food_serving_name_word VARCHAR," +
                        "food_energy DOUBLE," +
                        "food_proteins DOUBLE," +
                        "food_carbohydrates DOUBLE," +
                        "food_fat DOUBLE," +
                        "food_energy_calculated DOUBLE," +
                        "food_proteins_calculated DOUBLE," +
                        "food_carbohydrates_calculated DOUBLE," +
                        "food_fat_calculated DOUBLE," +
                        "food_user_id INT," +
                        "food_barcode VARCHAR," +
                        "food_category_id INT," +
                        "food_thumb VARCHAR," +
                        "food_image_a VARCHAR," +
                        "food_image_b VARCHAR," +
                        "food_image_c VARCHAR," +
                        "food_notes VARCHAR) ; ");


            } catch (SQLException e) {
                e.printStackTrace();


            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //!!!!!all tables to be dropped wll be listed below
            db.execSQL("DROP TABLE IF EXISTS food ");
            db.execSQL("DROP TABLE IF EXISTS categories");
            db.execSQL("DROP TABLE IF EXISTS food_diary");
            db.execSQL("DROP TABLE IF EXISTS food_diary_cal_eaten ");
            onCreate(db);

            String TAG = "Tag";
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        }//END PUBLIC VOID OnUpgrade
    }//DatabaseHelper


    /*Open database ------------------------------/*/
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /*close database ------------------------------/*/
    public void close() {
        DBHelper.close();
    }

    /*insert data ------------------------------/*/
    public void insert(String table, String fields, String values) {
        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
    }

    /*Count ------------------------------/*/
    public int count(String table) {
        Cursor mCount = db.rawQuery("SELECT COUNT (*) FROM " + table + "", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }
}
