package com.example.androidlab4;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    final String FILE_NAME = "Picture.png";
    ImageView profileImage;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Declaration
        EditText phoneInput = (EditText) findViewById(R.id.phoneInput);
        Button phoneButton = (Button) findViewById(R.id.phoneButton);
        Button imageButton = (Button) findViewById(R.id.imageButton);
        profileImage = (ImageView) findViewById(R.id.imageView);

        // Load image from save location
        File file = new File(getFilesDir(), FILE_NAME);
        if(file.exists()) {
            profileImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        // Get email from login screen and set it in the text field
        Intent fromPrevious = getIntent();
        ((TextView) findViewById(R.id.textView)).setText("Welcome back, " + fromPrevious.getStringExtra("EmailAddress") + "!");

        // Detect button press in the Call button and make a call request
        phoneButton.setOnClickListener(clk -> {
            if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent call = new Intent((Intent.ACTION_DIAL));
                call.setData(Uri.parse("tel:" + phoneInput.getText().toString()));
                startActivity(call);
            } else ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        });

        // Detect button press in Change Image button and open the resources to change the image and save / load the data
        imageButton.setOnClickListener(clk -> {

            if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                // Take photo
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Launch intent to open the camera
                cameraResult.launch(cameraIntent);
            } else ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        });
    }
    // Get photo data via the result launcher
    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {

            // If the result retrieved from the activity is ok, create the profile pic
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Bitmap thumbnail = data.getParcelableExtra("data");
                profileImage.setImageBitmap(thumbnail);
                mBitmap = thumbnail;
            }
        });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileOutputStream fOut;
        try {
            // Establish data stream to the specified location (FILE_NAME) and store the image
            fOut = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            // Close stream
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}