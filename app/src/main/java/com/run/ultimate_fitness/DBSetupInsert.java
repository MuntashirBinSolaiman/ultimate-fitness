package com.run.ultimate_fitness;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.run.ultimate_fitness.adapters.DBAdapter;

public class DBSetupInsert {
    //variables
    private  final Context context;

    //public class that other classes can add
    public DBSetupInsert(Context ctx) {
        this.context = ctx;
    }

    /*Setup Insert to categories----------------*/
    //To insert to category table

    public void  setupInsertToCategories(String values){
        try{
        //it will first open a new adapter DB
        DBAdapter db = new DBAdapter(context);
        //start connection to database
        db.open();
        //then insert table with its fields
        db.insert("categories",
                "_id, category_name, category_parent_id, category_icon, category_note",
                values);
        //close connection to database
        db.close();
    }
     catch(SQLiteException e)  {
            //Toast.makeText(Context, "Error; could not insert categories.", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertAllCategories(){
        setupInsertToCategories("NULL, 'Dairy and eggs', '0', '', NULL");
        setupInsertToCategories("NULL, 'Bread', '1', '', NULL");
        setupInsertToCategories("NULL, 'Meat', '6', '', NULL");
    }



    /*Setup Insert To categories*/
    //insert to food method to insert into food table
    public void setupInsertToFood(String values) {
        try {
            //make use of dbadapter
            DBAdapter db = new DBAdapter(context);
            db.open();

            db.insert("food", "_id, food_name, food_manufactor_name, food_serving_size, food_serving_measurement, food_serving_name_number, food_serving_name_word,  food_energy, food_proteins, " +
                    "food_carbohydrates, food_fat, food_energy_calculated, food_proteins_calculated, food_carbohydrates_calculated, food_fat_calculated, food_user_id, food_barcode, food_category_id, food_thumb, food_image_a," +
                    "food_image_b, food_image_c, food_notes ", values);
            db.close();

        } catch (SQLiteException e) {

        }
    }

    /*Insert all food into food database */
    public void insertAllFood(){
        setupInsertToFood("NULL, 'ProteinBar', 'Willards', '100' , 'grams', '1', 'stk', '314', '7' , '58', '6', '94', '2', '17', '1', '1k', '1', '1', 'e45', 'r56', 'dd4', 'itu7', '4f' ");
    }
}
