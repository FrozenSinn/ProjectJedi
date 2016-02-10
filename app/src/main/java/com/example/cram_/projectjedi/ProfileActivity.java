package com.example.cram_.projectjedi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ProfileActivity extends BaseActivity {

    public static final String PREFS_NAME = "prefs";
    SharedPreferences settings;

    List<Address> l;
    LocationManager lm;
    LocationListener lis;

    TextView tName, tHS;
    ImageView iPhoto;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_profile);

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
        Cursor c = users.getImage(name);
        if (c.moveToFirst()) {
            String content = c.getString(c.getColumnIndex("image"));
            if (content != "default") {
                Uri selectedImage = Uri.parse(content);
                try {
                    iPhoto.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        l = null;
        lm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        lis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder gc = new Geocoder(getApplicationContext());
                try {
                    l = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1; ++i) {
                    Log.v("LOG", l.get(i).getAddressLine(0).toString());
                    TextView t = (TextView) findViewById(R.id.textViewGPS);
                    if (i == 0) t.setText("");
                    t.setText(t.getText() + "\n" + l.get(i).getAddressLine(0).toString());
                }
                Log.v("LOG", ((Double) location.getLatitude()).toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lis);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lis);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(!drawer.isDrawerOpen(GravityCompat.START)) {
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
        }
        super.onBackPressed();
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
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    users.setImage(selectedImage.toString(), settings.getString("user", "No name"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
