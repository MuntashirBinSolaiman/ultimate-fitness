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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SignUpInformation extends AppCompatActivity {
//...
    private FirebaseAuth mAuth;
    private EditText firstNameTxt,lastNameTxt,phoneNumberTxt, weightNameTxt,heightNameTxt;
    private TextView addInfoButton;
    private ProgressBar progressBar;
    private ImageView profilePicImage;
    private String picture,workoutGoal = "";

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";
    public static final String WORKOUT_GOAL ="workoutGoal";
    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String PICTURE ="picture";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        firstNameTxt = findViewById(R.id.firstNameEditText);
        lastNameTxt = findViewById(R.id.lastNameEditText);
        phoneNumberTxt = findViewById(R.id.phoneNumberEditText);
        weightNameTxt = findViewById(R.id.weightEditTextNumberDecimal);
        heightNameTxt = findViewById(R.id.heightEditTextNumberDecimal);
        progressBar = findViewById(R.id.registerProgressBar);
        addInfoButton = findViewById(R.id.submitInformationButton);
        profilePicImage = findViewById(R.id.displayPic);
    }

    public void submitInformation(View view){
        registerUser();
    }

    public  void addProfilePic(View view){

        if(checkAndRequestPermissions(SignUpInformation.this)) {
            chooseImage(SignUpInformation.this);
        }
    }

    private void registerUser(){
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String weight = weightNameTxt.getText().toString();
        String height = heightNameTxt.getText().toString();

        if(firstName.isEmpty()){
            firstNameTxt.setError("First name is required");
            firstNameTxt.requestFocus();
            return;
        }

        if(lastName.isEmpty()){
            lastNameTxt.setError("Last name is required");
            lastNameTxt.requestFocus();
            return;
        }

        if(phoneNumber.isEmpty()){
            phoneNumberTxt.setError("Phone number is required");
            phoneNumberTxt.requestFocus();
            return;
        }

        if(weight.isEmpty()){
            weightNameTxt.setError("Weight is required");
            weightNameTxt.requestFocus();
            return;
        }

        if(height.isEmpty()){
            heightNameTxt.setError("Height is required");
            heightNameTxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        addInfoButton.setVisibility(View.GONE);

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
                            Toast.makeText(SignUpInformation.this,"User has been successfully registered", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.VISIBLE);
                            addInfoButton.setVisibility(View.GONE);

                            saveDataLocal(firstName,lastName,phoneNumber,weight,height,picture, workoutGoal);

                            Intent intent = new Intent(SignUpInformation.this, WorkoutsGoalPage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignUpInformation.this,"Failed to Register! Please try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            addInfoButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void saveDataLocal(String firstName,String lastName,String phoneNumber,String weight,String height,String picture,String workoutGoal){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(FIRST_NAME,firstName);
        editor.putString(LAST_NAME,lastName);
        editor.putString(PHONE_NUMBER,phoneNumber);
        editor.putString(WEIGHT,weight);
        editor.putString(HEIGHT,height);
       // editor.putString(WORKOUT_GOAL,workoutGoal);
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putString(PICTURE,picture);
        editor.apply();
    }

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
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                if (ContextCompat.checkSelfPermission(SignUpInformation.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(SignUpInformation.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(SignUpInformation.this);
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

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PICTURE,BitMapToString(selectedImage));
                        picture = BitMapToString(selectedImage);
                        editor.apply();

                        profilePicImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                profilePicImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                String bitMapAgain = BitMapToString(BitmapFactory.decodeFile(picturePath));
                                picture =BitMapToString(BitmapFactory.decodeFile(picturePath));
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(PICTURE,bitMapAgain);
                                editor.apply();

                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}