package com.example.androidlab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w(TAG, "In onCreate() - Loading Widgets");
        Log.d(TAG, "Message");

        Button loginButton = (Button) findViewById(R.id.loginButton);
        EditText emailEditText = (EditText) findViewById(R.id.emailInput);

        // Load email address from shared preference
        SharedPreferences prefs = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginData", "");

        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener(clk -> {
            // Store email address
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginData",  emailEditText.getText().toString());
            editor.apply();

            // Launch next page sending the email address
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailEditText.getText().toString());
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart() - The application is now visible on the screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume() - The application is now in focus and awaiting user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause() - The application state is frozen to allow for other processes");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop() - The application is no longer visible on the screen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "In onDestroy() - The application resources are being freed and closing operations occur");
    }
}