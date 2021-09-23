package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfile extends AppCompatActivity {

    private TextView toolDisplayView, toolLogoutView;
    private ImageView backButtonImage;
    private EditText firstNameTxt, lastNameTxt, phoneNumberEdit, heightTxt, weightTxt;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        toolDisplayView = findViewById(R.id.toolbarTextView);
        toolLogoutView = findViewById(R.id.toolbarLogoutTextView);
        backButtonImage =findViewById(R.id.toolbarBackButton);

        firstNameTxt = findViewById(R.id.firstNameChangeTextView);
        lastNameTxt = findViewById(R.id.lastNameChangeEditText);
        phoneNumberEdit = findViewById(R.id.phoneNumberChangeEditText);
        heightTxt = findViewById(R.id.heightChangeEditText);
        weightTxt =findViewById(R.id.weightChangeEditText);

        toolDisplayView.setText("Edit Profile");
        toolLogoutView.setVisibility(View.GONE);

        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, ProfilePage.class);
            startActivity(intent);
        });
    }

    public void editProfilePic(View view){    }

    public void updateUser(View view){

        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberEdit.getText().toString();
        String weight = weightTxt.getText().toString();
        String height = heightTxt.getText().toString();

        if(!firstName.isEmpty()){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(FIRST_NAME,firstName);
            editor.commit();
        }

        if(!lastName.isEmpty()){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LAST_NAME,lastName);
            editor.commit();
        }

        if(!phoneNumber.isEmpty()){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PHONE_NUMBER,phoneNumber);
            editor.commit();
        }

        if(!weight.isEmpty()){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(WEIGHT,weight);
            editor.commit();
        }

        if(!height.isEmpty()){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(HEIGHT,height);
            editor.commit();
        }

        //progressBar.setVisibility(View.VISIBLE);

        updateOnline();
    }

    private void updateOnline(){

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER,"");
        String weight = sharedPreferences.getString(WEIGHT,"");
        String height = sharedPreferences.getString(HEIGHT,"");

        int finalPhone = Integer.parseInt(phoneNumber);
        Double finalWeight = Double.parseDouble(weight);
        Double finalHeight = Double.parseDouble(height);

        User user = new User(firstName,lastName,finalPhone,finalWeight,finalHeight);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfile.this,"User has been successfully update", Toast.LENGTH_LONG).show();

                            //progressBar.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(EditProfile.this, ProfilePage.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EditProfile.this,"User failed Update! Check connection and please try again!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}