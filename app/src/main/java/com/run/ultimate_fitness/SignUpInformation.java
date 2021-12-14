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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.run.ultimate_fitness.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpInformation extends AppCompatActivity {
//...
    private FirebaseAuth mAuth;
    private EditText firstNameTxt,lastNameTxt, weightNameTxt,heightNameTxt;
    private TextView addInfoButton, phoneNumberTxt;
    private ProgressBar progressBar;
    private ImageView profilePicImage;
    private Bitmap bitmap;
    private String picture = "",workoutGoal = "";

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

    public static final String USER_UID = "uid";

    public String uid ="";



    public  DatabaseReference root ;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        phoneNumberTxt.setText(getStoredNum());
    }

    private void initChat() {
        String temp_fullName = firstNameTxt.getText().toString() + " " + lastNameTxt.getText().toString();
        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("users").child(uid);

        Map<String,Object> map1 = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        map1.put("name", temp_fullName);
        map1.put("image", picture);
        map1.put("uid", uid);
        map1.put("message", "Welcome to Ultimate Fitness\uD83D\uDCAA\uD83C\uDFFF");
        map1.put("lastUID", Constants.MASTER_UID);



        root.updateChildren(map1);

        sendWelcomeMessage();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void submitInformation(View view){

        if (isNetworkAvailable())
        {
            registerUser();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            progressBar.setVisibility(View.GONE);
            addInfoButton.setVisibility(View.VISIBLE);
        }

    }

    public  void addProfilePic(View view){

        if(checkAndRequestPermissions(SignUpInformation.this)) {
            chooseImage(SignUpInformation.this);
        }
    }

    public String getStoredNum(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, "01");

        return phoneNumber;
    }

    //This method uploads files to firebase
    private void registerUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, "01") ;
        String weight = weightNameTxt.getText().toString();
        String height = heightNameTxt.getText().toString();

        double checkWeight = Double.parseDouble(weight);
        double checkHeight = Double.parseDouble(height);

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher firstNameMatcher = pattern.matcher(firstName);
        Matcher lastNameMatcher = pattern.matcher(firstName);

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

        if(checkWeight < 30.00 || checkWeight > 150.00){
            weightNameTxt.setError("Weight must be between 30Kgs and 150Kgs");
            weightNameTxt.requestFocus();
            return;
        }

        if(checkHeight < 1.00 || checkHeight > 3.00){
            heightNameTxt.setError("Height must be between 1m and 3m");
            heightNameTxt.requestFocus();
            return;
        }

        boolean isFContainsSpecialCharacters = firstNameMatcher.find();
        boolean isLContainsSpecialCharacters = lastNameMatcher.find();

        if(isFContainsSpecialCharacters){
            firstNameTxt.setError("Cannot contain special characters");
            firstNameTxt.requestFocus();
            return;
        }

        if(isLContainsSpecialCharacters){
            lastNameTxt.setError("Cannot contain special characters");
            lastNameTxt.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        addInfoButton.setVisibility(View.GONE);

        int finalPhone = Integer.parseInt(phoneNumber);
        Double finalWeight = Double.parseDouble(weight);
        Double finalHeight = Double.parseDouble(height);

        updatePicture();

        User user = new User(firstName,lastName,finalPhone,finalWeight,finalHeight,picture,workoutGoal);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpInformation.this,"User has been successfully registered", Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.VISIBLE);
                        addInfoButton.setVisibility(View.GONE);

                        initChat();
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
                });
    }

    private void sendWelcomeMessage() {

        String fullName = firstNameTxt.getText().toString();

        //Sets the root to the current user's chat
        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference()
                .child("users")
                .child(uid)
                .child("chat");

        Map<String, Object> map1 = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        root.updateChildren(map1);

        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference message_root = root.child(temp_key);
        Map<String, Object> map2 = new HashMap<String, Object>();

        //Adds data to the RTDB
        map2.put("name", fullName);
        map2.put("message", "Welcome to Ultimate Fitness\uD83D\uDCAA\uD83C\uDFFF");
        map2.put("uid", Constants.MASTER_UID);
        map2.put("timestamp", System.currentTimeMillis());
        message_root.updateChildren(map2);
        message_root.updateChildren(map2);

    }

    //This method is used to save information to the shared preferences
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

    //Uploads a default picture incase user does not select one
    private void updatePicture(){
        if(picture.equals("") || picture.isEmpty()){
            picture = loadDefault();
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PICTURE, picture);
        editor.apply();
    }

    //Checks for permissions
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

    //Allocates Permissions
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

    //Opens intent for camera or gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        picture = BitMapToString(selectedImage);
                        profilePicImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if(resultCode == RESULT_OK && data != null){
                        Uri selectedImage = data.getData();

                        AsyncTask.execute(() -> {
                            //TODO your background code
                            try {
                                RequestOptions myOptions = new RequestOptions()
                                        .override(700, 700);

                                bitmap = Glide
                                        .with(SignUpInformation.this)
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
                        });

                        new ClearSpTask(output -> {
                            // you can go here
                            picture = BitMapToString(bitmap);
                            profilePicImage.setImageBitmap(bitmap);
                        }).execute();
                    }
                    break;
            }
        }
    }
    //Converts bitmap to string
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private String loadDefault(){
        String defaultPicture = "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g48AY2iISDdN5+dmv//266LIeF0SPiQi7W73/p07d2/fbLc71BeDjwBjmFSboq9fufL1wwdpmhpusjDGvLswbly98vAhx5diaBBgDBkRaXc6d27ffHD/XqfTFcbBY6+q75XLC99/83WWMjWCoUGAMXxEpNvpPrh/74u7t9tZRoPHmYh0ut2FhUt//+7bsiypL4YIAcZwEul2u18++OLerZv5vZfcAAAgAElEQVRtNruOq+pNR5fmZv/z+++qZ9Xq/kTAJyDAGGJp2v36y4d3b95g080YEpFuN52bnf7Pv1FfDCUCjCGmatI0/frLh3du3Wi32zR4fFQzz7Mz0//99+9VTUF9MYR83R8AOBdVzbLs26++tN6/frPcSGIyPPKq+l6am//Pv32jaoqi5N85hhEjYAw9VU2z7JsHXzy8fzdNU7bhjLZqG/zVywv/9R/fq5qiKKgvhhQjYIwCVe2m6YP7d733T1+8TOLYitDh0SMi7Xb35s3r33/9ZZblQZUJDwwvAowRoaqdTvfenZvOuUdPn0feee8ZDY8SVe10u3fv3Pr64YMsy1SV9mKoMQWN0VENj25fv/Z//v69MZLxJpxRISJlWWZZ9tWDL7758mFV37o/FHBeBBgjpToQ+NL83P/977834pj3B4+A6v2+Gsx/fP/dF3dudzod6ovRQIAxaqp3Nkw0W//3v/9zdma60+ER4SFWveDIR/7//Nffr15eaPOOI4wQAowRJCJplllr//s//n7j2vV2u82YaRiJSKfTnZqa+v/+6z+npyZ4wyBGDJuwMJpEpCgKa+3fvvt6amri+ctFa10UsS1rOIhICKHb7d66eePrhw80hJR3HGHkyNvVtbo/A9BHItJIkp29/UdPnnbStJEkNHjAiUiW58bI1w++uHXzepqmIQTqi9FDgDH6VDVJ4rwofnnyfGtrp9GIrbVkeDBVp1xNtlp/+/brqampLtPOGF0EGGNBVb333vnFN29evVmy1kZRRIMHiogUZZln2fWrV7/58oFzLuU9VxhpBBhjREQacby7f/D42fPjk5NmM+EshwFxutvZ+a++vH/j2vU8z4qipL4YbQQY40VVkyQJITx/+ert6qr3ngOz6vVuv1V2+dL81189aDWbTDtjTBBgjB1VddbFSbS5tf3kxctuJ0uSiFXhWlQDXyvy4Iv7d27dLIsiLwrqizFB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";
        return defaultPicture;
    }
}