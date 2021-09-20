package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText firstNameTxt,lastNameTxt,phoneNumberTxt, weightNameTxt,heightNameTxt;
    private ProgressBar progressBar;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";

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
    }

    public void submitInformation(View view){
        registerUser();
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

        //progressBar.setVisibility(View.VISIBLE);

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
                            Toast.makeText(SignUpInformation.this,"User has been successfully registered", Toast.LENGTH_LONG).show();

                            //progressBar.setVisibility(View.VISIBLE);

                            saveDataLocal(firstName,lastName,phoneNumber,weight,height);

                            Intent intent = new Intent(SignUpInformation.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignUpInformation.this,"Failed to Register! Please try again!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void saveDataLocal(String firstName,String lastName,String phoneNumber,String weight,String height){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(FIRST_NAME,firstName);
        editor.putString(LAST_NAME,lastName);
        editor.putString(PHONE_NUMBER,phoneNumber);
        editor.putString(WEIGHT,weight);
        editor.putString(HEIGHT,height);
        editor.commit();
    }
}