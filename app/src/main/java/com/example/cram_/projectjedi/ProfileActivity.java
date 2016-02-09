package com.example.cram_.projectjedi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ProfileActivity extends BaseActivity {

    public static final String PREFS_NAME = "prefs";

    SharedPreferences settings;

    TextView tName, tHS;
    ImageView iPhoto;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_profile);
        currentView = R.id.activityProfile;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);

        users = new Users(getApplicationContext());

        tName = (TextView) findViewById(R.id.textName);
        String name = settings.getString("user", "No name");
        tName.setText(name);
        tHS = (TextView) findViewById(R.id.textViewHS);
        String highScore = "High Score: " + Integer.toString(getHighScore(name));
        tHS.setText(highScore);
        iPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
    }

    public int getHighScore(String n) {
        Cursor c = users.getHighScore(n);
        if(c.moveToFirst())
            return c.getInt(c.getColumnIndex("score"));
        else return 0;
    }

    public void logout(View view) {
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("logged", false);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void selectPhoto(View view) {
        Intent pickAnImage = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickAnImage.setType("image/*");

        startActivityForResult(pickAnImage, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                data.getData();
                Uri selectedImage = data.getData();
                try {
                    iPhoto.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
