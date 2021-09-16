package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.run.ultimate_fitness.ui.home.HomeFragment;

public class SignUpInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fistNameTxt,lastNameTxt,phoneNumberTxt, weightNameTxt,heightNameTxt;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);

        mAuth = FirebaseAuth.getInstance();

        fistNameTxt = findViewById(R.id.firstNameEditText);
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
        String fistName = fistNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String weight = weightNameTxt.getText().toString();
        String height = heightNameTxt.getText().toString();

        if(fistName.isEmpty()){
            fistNameTxt.setError("First name is required");
            fistNameTxt.requestFocus();
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

        User user = new User(fistName,lastName,finalPhone,finalWeight,finalHeight);
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
                            Intent intent = new Intent(SignUpInformation.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignUpInformation.this,"Failed to Register! Please try again!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}