package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    private TextView toolDisplayView, toolLogoutView, updateDataButton;
    private ImageView backButtonImage, profilePicImage;
    private EditText firstNameTxt, lastNameTxt, phoneNumberEdit, heightTxt, weightTxt;
    private ProgressBar progressBar;
    private Bitmap bitmap;
    private String thePicture = "";
    public String workoutGoal = "";

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";
    public static final String PICTURE ="picture";
    public static final String WORKOUT_GOAL ="workoutGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        profilePicImage = findViewById(R.id.imageImageView);
        toolDisplayView = findViewById(R.id.toolbarTextView);
        updateDataButton = findViewById(R.id.updateUserButton);
        progressBar = findViewById(R.id.updateProgressbar);
        toolLogoutView = findViewById(R.id.toolbarLogoutTextView);
        backButtonImage =findViewById(R.id.toolbarBackButton);

        firstNameTxt = findViewById(R.id.firstNameChangeTextView);
        lastNameTxt = findViewById(R.id.lastNameChangeEditText);
        phoneNumberEdit = findViewById(R.id.phoneNumberChangeEditText);
        heightTxt = findViewById(R.id.heightChangeEditText);
        weightTxt =findViewById(R.id.weightChangeEditText);

        toolDisplayView.setText("Edit Profile");

        loadImage();

        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, ProfilePage.class);
            startActivity(intent);
        });

        toolLogoutView.setText("Change Goals");
        toolLogoutView.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, WorkoutsGoalPage.class);
            startActivity(intent);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void editProfilePic(View view){

        if(checkAndRequestPermissions(EditProfile.this)) {
            chooseImage(EditProfile.this);
        }
    }

    //Gets information from fields
    public void updateUser(View view){

        if (isNetworkAvailable())
        {
            String firstName = firstNameTxt.getText().toString();
            String lastName = lastNameTxt.getText().toString();
            String phoneNumber = phoneNumberEdit.getText().toString();
            String weight = weightTxt.getText().toString();
            String height = heightTxt.getText().toString();

            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");

            if(!firstName.isEmpty()){
                Matcher firstNameMatcher = pattern.matcher(firstName);
                boolean isFContainsSpecialCharacters = firstNameMatcher.find();
                if(isFContainsSpecialCharacters){
                    firstNameTxt.setError("Cannot contain special characters");
                    firstNameTxt.requestFocus();
                    return;
                }


                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(FIRST_NAME,firstName);
                editor.apply();
            }

            if(!lastName.isEmpty()){
                Matcher lastNameMatcher = pattern.matcher(firstName);
                boolean isLContainsSpecialCharacters = lastNameMatcher.find();
                if(isLContainsSpecialCharacters){
                    lastNameTxt.setError("Cannot contain special characters");
                    lastNameTxt.requestFocus();
                    return;
                }

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LAST_NAME,lastName);
                editor.apply();
            }

            if(!phoneNumber.isEmpty()){
                if(phoneNumber.length() != 10){
                    phoneNumberEdit.setError("Please enter valid phone number");
                    phoneNumberEdit.requestFocus();
                    return;
                }
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PHONE_NUMBER,phoneNumber);
                editor.apply();
            }

            if(!weight.isEmpty()){

                Double checkWeight = Double.parseDouble(weight);

                if(checkWeight < 30.00 || checkWeight > 150.00){
                    weightTxt.setError("Weight must be between 30Kgs and 150Kgs");
                    weightTxt.requestFocus();
                    return;
                }
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WEIGHT,weight);
                editor.apply();
            }

            if(!height.isEmpty()){
                Double checkHeight = Double.parseDouble(height);

                if(checkHeight < 1.00 || checkHeight > 3.00){
                    heightTxt.setError("Height must be between 1m and 3m");
                    heightTxt.requestFocus();
                    return;
                }
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(HEIGHT,height);
                editor.apply();
            }

            try{
                progressBar.setVisibility(View.VISIBLE);
                updateDataButton.setVisibility(View.GONE);

                updateOnline();
            }
            catch (Exception e)
            {
                progressBar.setVisibility(View.GONE);
                updateDataButton.setVisibility(View.VISIBLE);
                Toast.makeText(EditProfile.this,"Please make a change before uploading", Toast.LENGTH_LONG).show();
            }

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            progressBar.setVisibility(View.GONE);
            updateDataButton.setVisibility(View.VISIBLE);
        }



    }

    //Uploads chances to fire store database
    private void updateOnline(){

        if(!thePicture.equals("") && !thePicture.equals(null)){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PICTURE, thePicture);
            editor.apply();
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER,"");
        String weight = sharedPreferences.getString(WEIGHT,"");
        String height = sharedPreferences.getString(HEIGHT,"");
        String picture = sharedPreferences.getString(PICTURE,"");
        String workoutGoal = sharedPreferences.getString(WORKOUT_GOAL,"");


        int finalPhone = Integer.parseInt(phoneNumber);
        Double finalWeight = Double.parseDouble(weight);
        Double finalHeight = Double.parseDouble(height);


        User user = new User(firstName,lastName,finalPhone,finalWeight,finalHeight,picture,workoutGoal);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfile.this,"User has been successfully update", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.VISIBLE);
                            updateDataButton.setVisibility(View.GONE);


                            Intent intent = new Intent(EditProfile.this, ProfilePage.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EditProfile.this,"User failed Update! Check connection and please try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            updateDataButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(EditProfile.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(EditProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(EditProfile.this);
                }
                break;
        }
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        thePicture = BitMapToString(selectedImage);
                        profilePicImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if(resultCode == RESULT_OK && data != null){
                        Uri selectedImage = data.getData();

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                                try {
                                    RequestOptions myOptions = new RequestOptions()
                                            .override(700, 700);

                                    bitmap = Glide
                                            .with(EditProfile.this)
                                            .asBitmap()
                                            .apply(myOptions)
                                            .load(selectedImage)
                                            .submit()
                                            .get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        new ClearSpTask(new ClearSpTask.AsynResponse() {
                            @Override
                            public void processFinish(Boolean output) {
                                // you can go here
                                thePicture = BitMapToString(bitmap);
                                profilePicImage.setImageBitmap(bitmap);
                            }
                        }).execute();

                    }
                    break;
            }
        }
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        profilePicImage.setImageBitmap(StringToBitMap(picture));
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void loadImage(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }
}
