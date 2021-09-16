package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.run.ultimate_fitness.ui.home.HomeFragment;

public class SignUpInformation extends AppCompatActivity {
    private EditText fistNameTxt;
    private EditText lastNameTxt;
    private EditText phoneNumberTxt;
    private EditText weightNameTxt;
    private EditText heightNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);

        fistNameTxt = findViewById(R.id.firstNameEditText);
        lastNameTxt = findViewById(R.id.lastNameEditText);
        phoneNumberTxt = findViewById(R.id.phoneNumberEditText);
        weightNameTxt = findViewById(R.id.weightEditTextNumberDecimal);
        heightNameTxt =findViewById(R.id.heightEditTextNumberDecimal);
    }

    public void submitInformation(View view){
        String fistName = fistNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String weight = weightNameTxt.getText().toString();
        String height = heightNameTxt.getText().toString();

         //int finalPhone = Integer.parseInt(phoneNumber);
        //Double finalWeight = Double.parseDouble(weight);
        //Double finalHeight = Double.parseDouble(height);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}