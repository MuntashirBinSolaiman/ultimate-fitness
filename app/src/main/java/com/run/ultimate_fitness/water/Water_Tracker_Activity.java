package com.run.ultimate_fitness.water;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.run.ultimate_fitness.MainActivity;
import com.run.ultimate_fitness.R;

import java.text.DateFormat;
import java.util.Calendar;

public class Water_Tracker_Activity extends AppCompatActivity {
    private int cups_of_water = 0;
    //private int id = 0;
    private ProgressBar progressBar;
    private TextView text_view_progress;
    private TextView tvDate;
    private ListView lv_view;
    private WaterModel waterModel;
   // private EditText et_size_of_cup;
    private ArrayAdapter waterArrayAdapter;
    private Water_Tracker_DOBHelper waterTrackerDobHelper;

    public static final String USER_PREFS ="userPrefs";
    public static final String WATER ="water";


    Button insert, update, delete, view, open_calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_tracker_activity_main);



        text_view_progress = findViewById(R.id.text_view_progress);
        progressBar = findViewById(R.id.progress_bar);
        open_calendar = findViewById(R.id.open_calendar);
        tvDate = findViewById(R.id.tv_date);
       // et_size_of_cup = findViewById(R.id.et_size_of_cup);
        insert = findViewById(R.id.add_to_database);
        view = findViewById(R.id.view_table);
        delete = findViewById(R.id.delete_table);
        lv_view = findViewById(R.id.lv_view);

        waterTrackerDobHelper = new Water_Tracker_DOBHelper(Water_Tracker_Activity.this);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        cups_of_water = sharedPreferences.getInt(WATER, 0);
        text_view_progress.setText("" + cups_of_water );


        //ShowWaterEntryOnListView(dobHelper);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        open_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Water_Tracker_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                        TextView tvDate = (TextView) findViewById(R.id.tv_date);
                        tvDate.setText(currentDateString);

                        month = month + 1;
                        String date = day +"/"+ month +"/" + year;
                        tvDate.setText(date);
                    }
                }, year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WaterModel waterModel;

                try {
                    waterModel = new WaterModel(-1, tvDate.getText().toString(),
                            Integer.parseInt(text_view_progress.getText().toString()));
                    Toast.makeText(Water_Tracker_Activity.this, waterModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(Water_Tracker_Activity.this, "Error creating water entry", Toast.LENGTH_SHORT).show();
                    waterModel = new WaterModel(-1, "error", 0);
                }
                Water_Tracker_DOBHelper waterTrackerDobHelper = new Water_Tracker_DOBHelper(Water_Tracker_Activity.this);
                Boolean success = waterTrackerDobHelper.addWaterProgress(waterModel);
                //ShowWaterEntryOnListView(dobHelper);
                Toast.makeText(Water_Tracker_Activity.this, "Success" + " " + success, Toast.LENGTH_SHORT).show();
                waterArrayAdapter = new ArrayAdapter<WaterModel>(Water_Tracker_Activity.this,
                        android.R.layout.simple_list_item_1, waterTrackerDobHelper.getInfo());
                lv_view.setAdapter(waterArrayAdapter);
            }



        });


        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WaterModel clickedEntry = (WaterModel) parent.getItemAtPosition(position);
                waterTrackerDobHelper.deleteOne(clickedEntry);
                waterArrayAdapter = new ArrayAdapter<WaterModel>(Water_Tracker_Activity.this,
                        android.R.layout.simple_list_item_1, waterTrackerDobHelper.getInfo());
                lv_view.setAdapter(waterArrayAdapter);
                Toast.makeText(Water_Tracker_Activity.this, "Entry  " + " " + clickedEntry + " " + "Deleted", Toast.LENGTH_SHORT).show();

            }
            });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Water_Tracker_DOBHelper waterTrackerDobHelper = new Water_Tracker_DOBHelper(Water_Tracker_Activity.this);
                //Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_SHORT).show();

                //Display
                //ShowWaterEntryOnListView(dobHelper);



                waterArrayAdapter = new ArrayAdapter<WaterModel>(Water_Tracker_Activity.this,
                        android.R.layout.simple_list_item_1, waterTrackerDobHelper.getInfo());



                if(waterArrayAdapter.getCount() == 0){

                    lv_view.setAdapter(waterArrayAdapter);

                }else{
                    Toast.makeText(Water_Tracker_Activity.this, "Entry empty, Please add Progress", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateD = tvDate.getText().toString();
                waterModel = new WaterModel(-1, tvDate.getText().toString(),
                        Integer.parseInt(text_view_progress.getText().toString()));

                Water_Tracker_DOBHelper waterTrackerDobHelper =
                        new Water_Tracker_DOBHelper(Water_Tracker_Activity.this);

                Boolean checkdelete = waterTrackerDobHelper.deleteAll(waterModel);

                if(checkdelete)
                    Toast.makeText(Water_Tracker_Activity.this, "Progress Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Water_Tracker_Activity.this, "Progress Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });




    }

    /*private void ShowWaterEntryOnListView(DOBHelper dobHelper) {
        waterArrayAdapter = new ArrayAdapter<WaterModel>(MainActivity.this,
                android.R.layout.simple_list_item_1, dobHelper.getInfo());
    }*/

    public void drinkWater(View view) {
        if (cups_of_water <= 99) {
            cups_of_water++;
            String waterDrank = String.valueOf(cups_of_water);
            String waterProgress = waterDrank + "";
            text_view_progress.setText("" + cups_of_water );




            System.out.println(waterProgress);
            updateProgressBar();
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER, cups_of_water);
        editor.apply();

    }

    public void remove_drinkWater(View view) {
        if (cups_of_water >= 1) {
            cups_of_water--;
            String waterDrank = String.valueOf(cups_of_water);
            String waterProgress = waterDrank + "";
            text_view_progress.setText("" + cups_of_water );

            System.out.println(waterProgress);
            updateProgressBar();
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER, cups_of_water);
        editor.apply();

    }

    public void updateProgressBar() {
        progressBar.setProgress(cups_of_water);
    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Water_Tracker_Activity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}