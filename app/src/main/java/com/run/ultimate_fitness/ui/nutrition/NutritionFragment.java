package com.run.ultimate_fitness.ui.nutrition;

/* Author: Takunda Ziki
        *  Last modified: 28-10-2021 */

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.RecyclerViewData;
import com.run.ultimate_fitness.RemoveClickListner;
import com.run.ultimate_fitness.adapters.RecyclerAdapter;

import java.util.ArrayList;

public class NutritionFragment extends Fragment implements RemoveClickListner {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewDinner;
    private RecyclerView mRecyclerViewExercise;
    private RecyclerView mRecyclerViewSnacks;
    private RecyclerAdapter mRecyclerAdapter, mRecyclerAdapterLunch, mRecyclerAdapterDinner, mRecyclerAdapterSnacks, mRecyclerAdapterExcercise;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String CALORIES_GOAL ="calories_goal";

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String USER_PREFS ="userPrefs";
    public static final String GOALS_PREFS ="goalsPrefs";


    private ImageView profilePicImage;
    private TextView userName;

    TextView btnAddItem, btnAddItemLunch, btnAddItemDinner, btnAddItemSnacks, btnAddItemExcercise, btnAdd;
    ArrayList<RecyclerViewData> myList = new ArrayList<>();
    ArrayList<RecyclerViewData> myListLunch = new ArrayList<>();

    ArrayList<RecyclerViewData> myListDinner = new ArrayList<>();
    ArrayList<RecyclerViewData> myListExcercise = new ArrayList<>();
    ArrayList<RecyclerViewData> myListSnacks = new ArrayList<>();
    TextView textViewGoal, textViewFood, textViewExcercise, textViewRemaining;
    EditText etTitle, etDescription;
    String title = "", description = "";
    ImageView crossImage;
    Dialog builder;
    int totalFood = 0, totalExcercise = 0, totalGoal = 0, totalProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(GOALS_PREFS, MODE_PRIVATE);
        totalGoal = sharedPreferences.getInt(CALORIES_GOAL, 0);




        // View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerbreakfast_view);
        mRecyclerViewDinner = (RecyclerView) view.findViewById(R.id.recyclerDinner_view);
        mRecyclerViewExercise = (RecyclerView) view.findViewById(R.id.recyclerExcercise_view);
        mRecyclerViewSnacks = (RecyclerView) view.findViewById(R.id.recyclerSnacks_view);
        RecyclerView mRecyclerViewLunch = (RecyclerView) view.findViewById(R.id.recyclerLunch_view);
        textViewGoal = view.findViewById(R.id.textGoal);
        textViewGoal.setText(String.valueOf(sharedPreferences.getInt(CALORIES_GOAL, 0)));

        textViewFood = view.findViewById(R.id.textFood);

        textViewExcercise = view.findViewById(R.id.textExcercise);
        textViewRemaining = view.findViewById(R.id.textTotal);


        //Attach Linear Layout with  breakfast
        mRecyclerAdapter = new RecyclerAdapter(myList, this, "1");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        //Attach Linear Layout with mRecyclerViewLunch
        mRecyclerAdapterLunch = new RecyclerAdapter(myListLunch, this, "2");
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewLunch.setLayoutManager(layoutManager1);
        mRecyclerViewLunch.setAdapter(mRecyclerAdapterLunch);

        //Attach Linear Layout with mRecyclerViewDinner
        mRecyclerAdapterDinner = new RecyclerAdapter(myListDinner, this, "3");
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewDinner.setLayoutManager(layoutManager2);
        mRecyclerViewDinner.setAdapter(mRecyclerAdapterDinner);

        //Attach Linear Layout with mRecyclerViewExercise
        mRecyclerAdapterExcercise = new RecyclerAdapter(myListExcercise, this, "5");
        final LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewExercise.setLayoutManager(layoutManager3);
        mRecyclerViewExercise.setAdapter(mRecyclerAdapterExcercise);

        //Attach Linear Layout with mRecyclerViewSnacks
        mRecyclerAdapterSnacks = new RecyclerAdapter(myListSnacks, this, "4");
        final LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewSnacks.setLayoutManager(layoutManager4);
        mRecyclerViewSnacks.setAdapter(mRecyclerAdapterSnacks);

        btnAddItem = (TextView) view.findViewById(R.id.btnAddItemBreakfast);
        btnAddItemLunch = (TextView) view.findViewById(R.id.btnAddItemLunch);
        btnAddItemDinner = (TextView) view.findViewById(R.id.btnAddItemDinner);
        btnAddItemSnacks = (TextView) view.findViewById(R.id.btnAddItemSnacks);
        btnAddItemExcercise = (TextView) view.findViewById(R.id.btnAddItemExcercise);

        profilePicImage = view.findViewById(R.id.icon_user);
        userName = view.findViewById(R.id.txtUsername);

        loadImage();

        //Button Click for adding Goal
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            //we are sending the category id from here , like we are sending '1' as parameter that means it will
            //open the view for adding calories for Breakfast
            @Override
            public void onClick(View v) {
                AddProduct("1");
            }
        });
        btnAddItemLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct("2");
            }
        });
        btnAddItemDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct("3");
            }
        });
        btnAddItemSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct("4");
            }
        });
        btnAddItemExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct("5");
            }
        });
        return view;

    }

    //Remove Calories category wise
    @Override
    public void OnRemoveClick(int index, ArrayList<RecyclerViewData> myList, String value) {
        if (value.equals("1")) {
            //remove calories for Breakfast , myList is arraylist of model type RecyclerViewData
            //by using  myList.remove(index) function we can remove data from given index
            totalFood = totalFood - Integer.parseInt(myList.get(index).getDescription());
            textViewFood.setText(String.valueOf(totalFood));
            textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
            myList.remove(index);

            mRecyclerAdapter.notifyData(myList);
        } else if (value.equals("2")) {
            //remove calories for Lunch ,myList is arraylist of model type RecyclerViewData
             //by using  myList.remove(index) function we can remove data from given index
            totalFood = totalFood - Integer.parseInt(myList.get(index).getDescription());
            textViewFood.setText(String.valueOf(totalFood));
            textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
            myList.remove(index);
            //Notifying Recyclerview to update new data on views
            mRecyclerAdapterLunch.notifyData(myList);
        } else if (value.equals("3")) {
            //remove calories for Dinner
            totalFood = totalFood - Integer.parseInt(myList.get(index).getDescription());
            textViewFood.setText(String.valueOf(totalFood));
            textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
            myList.remove(index);

            //Notifying Recyclerview to update new data on views
            mRecyclerAdapterDinner.notifyData(myList);
        } else if (value.equals("4")) {
            //remove calories for Snacks
            Toast.makeText(getActivity(), "" + String.valueOf(totalFood), Toast.LENGTH_SHORT).show();
            totalFood = totalFood - Integer.parseInt(myList.get(index).getDescription());
            textViewFood.setText(String.valueOf(totalFood));
            textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
            myList.remove(index);
            //Notifying Recyclerview to update new data on views
            mRecyclerAdapterSnacks.notifyData(myList);
        } else if (value.equals("5")) {
            //remove calories for Excercise
            totalExcercise = totalExcercise - Integer.parseInt(myList.get(index).getDescription());

            textViewExcercise.setText(String.valueOf(totalExcercise));
            textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
            //remove Excercise from given index
            myList.remove(index);
            //Notifying Recyclerview to update new data on views
            mRecyclerAdapterExcercise.notifyData(myList);
        }


    }

    //Add Total Calories For each Categories
    public void AddProduct(String btn) {
        //open alert dialog
        builder = new Dialog(getActivity());
        builder.setContentView(R.layout.add_food);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText etTitle = builder.findViewById(R.id.etTitle);
        etTitle.setHint("Add Food/Exercise");
        EditText etDescription = builder.findViewById(R.id.etDescription);
        Button tvDone = builder.findViewById(R.id.btnAddItem);
        //Imageview to close the view , using which we are adding calories data
        ImageView imageView = builder.findViewById(R.id.close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });


        //btnDone for adding breakfast,lunch,dinner,snacks and exercise data on arraylist
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etTitle.getText().toString();
                description = etDescription.getText().toString();
                if (title.matches("")) {
                    Toast.makeText(getActivity(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.matches("")) {
                    Toast.makeText(getActivity(), "You did not enter a description", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Model class for adding title and description of calories data
                RecyclerViewData mLog = new RecyclerViewData();
                mLog.setTitle(title);
                mLog.setDescription(description);

                if (btn.equalsIgnoreCase("1")) {
                    //add calories for Breakfast
                    totalFood = Integer.parseInt(description) + totalFood;
                    textViewFood.setText(String.valueOf(totalFood));
                    textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                    myList.add(mLog);
                    mRecyclerAdapter.notifyData(myList);
                    etTitle.setText("");
                    etDescription.setText("");
                } else if (btn.equalsIgnoreCase("2")) {
                    //add calories for Lunch
                    totalFood = Integer.parseInt(description) + totalFood;
                    textViewFood.setText(String.valueOf(totalFood));
                    textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                    myListLunch.add(mLog);
                    mRecyclerAdapterLunch.notifyData(myListLunch);
                    etTitle.setText("");
                    etDescription.setText("");
                } else if (btn.equalsIgnoreCase("3")) {
                    //add calories for Dinner
                    totalFood = Integer.parseInt(description) + totalFood;
                    textViewFood.setText(String.valueOf(totalFood));
                    textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                    myListDinner.add(mLog);
                    mRecyclerAdapterDinner.notifyData(myListDinner);
                    etTitle.setText("");
                    etDescription.setText("");
                } else if (btn.equalsIgnoreCase("4")) {

                    //add calories for Snacks
                    totalFood = Integer.parseInt(description) + totalFood;
                    textViewFood.setText(String.valueOf(totalFood));
                    textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                    myListSnacks.add(mLog);
                    mRecyclerAdapterSnacks.notifyData(myListSnacks);
                    etTitle.setText("");
                    etDescription.setText("");
                } else if (btn.equalsIgnoreCase("5")) {
                    //add calories for Excercise
                    totalExcercise = Integer.parseInt(description) + totalExcercise;
                    textViewExcercise.setText(String.valueOf(totalExcercise));

                    textViewFood.setText(String.valueOf(totalFood));
                    textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                    myListExcercise.add(mLog);
                    mRecyclerAdapterExcercise.notifyData(myListExcercise);
                    etTitle.setText("");
                    etDescription.setText("");
                }

                //refresh Recyclerviews with Updated Data
                mRecyclerAdapter.notifyDataSetChanged();
                mRecyclerAdapterLunch.notifyDataSetChanged();
                mRecyclerAdapterDinner.notifyDataSetChanged();
                mRecyclerAdapterSnacks.notifyDataSetChanged();
                mRecyclerAdapterExcercise.notifyDataSetChanged();

                builder.dismiss();

            }
        });
        builder.show();
    }

    //Add Calories Goal here
    public void AddGoal() {
        //open alert dialog to add Goal data
        builder = new Dialog(getActivity());
        builder.setContentView(R.layout.add_goal);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button tvDone = builder.findViewById(R.id.btnAddItem);

        EditText etTitle = builder.findViewById(R.id.etGoal);
        etTitle.setHint("Goal Daily Calories");
        ImageView imageView = builder.findViewById(R.id.close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });


        //after adding Goal ,code for submit button
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(GOALS_PREFS, MODE_PRIVATE);

                if (title.matches("")) {
                    Toast.makeText(getActivity(), "You did not enter your Goal", Toast.LENGTH_SHORT).show();
                    return;
                }
                //set Goal in textview
                btnAdd.setText(title);
                totalGoal = Integer.parseInt(title);
                textViewGoal.setText(String.valueOf(title));
                //Calculate Remaining Calories=totalGoal-(totalFood + totalExcercise)
                textViewRemaining.setText(String.valueOf(totalGoal - (totalFood + totalExcercise)));
                //calculate progress
                totalProgress = totalGoal - (totalGoal - (totalFood + totalExcercise));
                //remove alert Dialog
                builder.dismiss();

            }
        });
        builder.show();
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public  void loadImage() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE, "");
        String firstName = sharedPreferences.getString(FIRST_NAME, "");
        String lastName = sharedPreferences.getString(LAST_NAME, "");


        userName.setText("Welcome, " + firstName);
        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

}

