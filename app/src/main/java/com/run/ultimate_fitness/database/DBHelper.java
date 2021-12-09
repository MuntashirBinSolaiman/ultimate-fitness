package com.run.ultimate_fitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.run.ultimate_fitness.RecyclerViewData;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MY_DATABASE";
    // User table name
    private static final String TABLE_NAME = "MY_TABLE";
    // User Table Columns names

    private static final String category_id = "id";
    private static final String item_id = "item_id";
    private static final String title= "title";
    private static final String description = "description";
    private final Context context;
    private String CREATE_TABLE= "CREATE TABLE " + TABLE_NAME + "(" + item_id + " INTEGER PRIMARY KEY, " + category_id + " INTEGER," + title + " TEXT," + description+ " TEXT " + ")";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    public void addItem(int category_id, String title, String description)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.category_id,category_id);
        values.put(DBHelper.title,title);
        values.put(DBHelper.description,description);
        long status=db.insert(TABLE_NAME,null,values);
        if(status<=0){
            Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Insertion Successful", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
// delete user record by id
        long s=db.delete(TABLE_NAME, item_id + " = ?",
                new String[]{String.valueOf(id)});
        if(s<=0){
            Toast.makeText(context, "Deletion Unsuccessful", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Deletion Successful", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void deleteAllItems(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM MY_TABLE");
    }
    public ArrayList<RecyclerViewData> load(int category_id)
    {
        RecyclerViewData mLog ;
        ArrayList<RecyclerViewData> arrayList=new ArrayList<>();
//        String result = "";
        String query = "Select*FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            mLog = new RecyclerViewData();
            if (cursor.getInt(1)==category_id) {
                int result_0 = cursor.getInt(0);
                int result_1 = cursor.getInt(1);
                String result_2 = cursor.getString(2);
                String result_3 = cursor.getString(3);
                mLog.setItem_id(result_0);
                mLog.setCategory_id(result_1);
                mLog.setTitle(result_2);
                mLog.setDescription(result_3);
//            String result_3 = cursor.getString(3);
//            result += String.valueOf(result_0) + " " + result_1 + " " + result_2 + " " + result_3 + "\n";
//            System.getProperty("line.separator");
                arrayList.add(mLog);
            }
        }
        cursor.close();
        db.close();
        return arrayList;
    }
}