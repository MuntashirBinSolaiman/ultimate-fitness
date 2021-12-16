package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangeEmailActivity extends AppCompatActivity {

    private TextView emailTextView, changeEmailButton;
    private ProgressBar progressBar;
    private EditText newEmailEditText;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
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

        changeEmailButton = findViewById(R.id.changeEmailButton);
        emailTextView = findViewById(R.id.emailTextView);
        progressBar = findViewById(R.id.forgotEmailProgressbar);
        newEmailEditText = findViewById(R.id.newNumberEditText);

        TextView toolBarTitle = findViewById(R.id.toolbarTextView);
        ImageView backImage = findViewById(R.id.toolbarBackButton);
        TextView toolBarLogOut = findViewById(R.id.toolbarLogoutTextView);

        toolBarLogOut.setVisibility(View.GONE);
        toolBarTitle.setText(R.string.change_email);

        backImage.setOnClickListener(v -> {

            Intent intent = new Intent(ChangeEmailActivity.this, ProfilePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        getCurrentUser();
    }

    private void getCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(EMAIL,"");
        String password = sharedPreferences.getString(PASSWORD,"");

        AuthCredential credential = EmailAuthProvider.getCredential(email,password);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // Name, email address, and profile photo Url
                String oldEmail = user.getEmail();
                emailTextView.setText(oldEmail);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void changeEmail(View view) {
        if(!isNetworkAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }

        String email = newEmailEditText.getText().toString().trim();

        if(email.isEmpty()){
            newEmailEditText.setError("Please enter email");
            newEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            newEmailEditText.setError("Please enter a valid email address");
            newEmailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        changeEmailButton.setVisibility(View.GONE);

        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        changeEmailButton.setVisibility(View.VISIBLE);

                        Toast.makeText(ChangeEmailActivity.this,"Email updated successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ChangeEmailActivity.this, ProfilePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

    }
}