package com.run.ultimate_fitness;

import static com.run.ultimate_fitness.SendOTPActivity.PHONE_NUMBER;
import static com.run.ultimate_fitness.SendOTPActivity.USER_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ChangePhoneNumber extends AppCompatActivity {

    LinearLayout linearLayout1,linearLayout2;
    TextView phoneNumberTextView, verifyOTPButton, changePhoneNumberButton;
    private ProgressBar progressBar;
    private EditText newNumberEditText,inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String verification, selectedCountryCode;
    CountryCodePicker ccp;

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        selectedCountryCode = ccp.getSelectedCountryCodeWithPlus();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if(!isNetworkAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }

        progressBar = findViewById(R.id.forgotEmailProgressbar);
        newNumberEditText = findViewById(R.id.newNumberEditText);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        verifyOTPButton = findViewById(R.id.verifyOTPButton);
        changePhoneNumberButton = findViewById(R.id.changeEmailButton);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.LinearLayout2);


        TextView toolBarTitle = findViewById(R.id.toolbarTextView);
        ImageView backImage = findViewById(R.id.toolbarBackButton);
        TextView toolBarLogOut = findViewById(R.id.toolbarLogoutTextView);

        toolBarLogOut.setVisibility(View.GONE);
        toolBarTitle.setText(R.string.change_phone_number);

        backImage.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePhoneNumber.this, ProfilePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });

        displayNumber();

        findViewById(R.id.textRespondOTP).setOnClickListener(v -> {

            inputCode1.setText("");
            inputCode2.setText("");
            inputCode3.setText("");
            inputCode4.setText("");
            inputCode5.setText("");
            inputCode6.setText("");

            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            verifyOTPButton.setVisibility(View.GONE);
            changePhoneNumberButton.setVisibility(View.VISIBLE);
            newNumberEditText.setVisibility(View.VISIBLE);

        });

        ccp.setOnCountryChangeListener(selectedCountry -> {
            Toast.makeText(getApplicationContext(), "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            selectedCountryCode = ccp.getSelectedCountryCodeWithPlus();
        });
    }

    private void displayNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(EMAIL,"");
        String password = sharedPreferences.getString(PASSWORD,"");

        AuthCredential credential = EmailAuthProvider.getCredential(email,password);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // Name, email address, and profile photo Url
                String oldPhoneNumber = user.getPhoneNumber();
                phoneNumberTextView.setText(oldPhoneNumber);
            }
        });
    }

    public void changePhoneNumber(View view) {
        if(newNumberEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(ChangePhoneNumber.this,"Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }


        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        verifyOTPButton.setVisibility(View.VISIBLE);
        changePhoneNumberButton.setVisibility(View.GONE);
        newNumberEditText.setVisibility(View.GONE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                selectedCountryCode + newNumberEditText.getText().toString(),
                60,
                TimeUnit.SECONDS,
                ChangePhoneNumber.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //progressBar.setVisibility(View.GONE);
                        //buttonGetOTP.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        linearLayout1.setVisibility(View.GONE);
                        linearLayout2.setVisibility(View.GONE);
                        verifyOTPButton.setVisibility(View.GONE);
                        changePhoneNumberButton.setVisibility(View.VISIBLE);
                        newNumberEditText.setVisibility(View.VISIBLE);


                        Toast.makeText(ChangePhoneNumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verification = verificationId;
                        setupOTPInput();
                    }
                }
        );
    }

    public void verifyOTP(View view) {
        if(!isNetworkAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
            return;
        }

        if(inputCode1.getText().toString().trim().isEmpty()
                || inputCode2.getText().toString().trim().isEmpty()
                || inputCode3.getText().toString().trim().isEmpty()
                || inputCode4.getText().toString().trim().isEmpty()
                || inputCode5.getText().toString().trim().isEmpty()
                || inputCode6.getText().toString().trim().isEmpty()){
            Toast.makeText(ChangePhoneNumber.this, "Please valid code", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = inputCode1.getText().toString() +
                inputCode2.getText().toString() +
                inputCode3.getText().toString() +
                inputCode4.getText().toString() +
                inputCode5.getText().toString() +
                inputCode6.getText().toString();

        if(verification != null){
            progressBar.setVisibility(View.VISIBLE);
            verifyOTPButton.setVisibility(View.GONE);

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                    verification,
                    code
            );
            user.updatePhoneNumber(phoneAuthCredential)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        verifyOTPButton.setVisibility(View.VISIBLE);
                        if(task.isSuccessful()){

                            SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PHONE_NUMBER, newNumberEditText.getText().toString());
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(),ProfilePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ChangePhoneNumber.this,"The verification code is invalid", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupOTPInput(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}