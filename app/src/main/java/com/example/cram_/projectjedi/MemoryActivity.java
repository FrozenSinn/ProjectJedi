package com.example.cram_.projectjedi;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryActivity extends BaseActivity implements View.OnClickListener{

    int[] imageURI;
    int backimage, blank;


    ArrayList<Integer> randomImage;



    Users users;
    SharedPreferences settings;
    public static final String PREFS_NAME = "prefs";

    boolean flipping_second = false;
    int score = 0;
    int left = 8;
    ImageView iBefore;
    int pBefore;

    TextView tScore;
    ImageView i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16;
    CoolImageFlipper coolImageFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        backimage = R.drawable.jediimage;
        blank = R.drawable.blank;
        imageURI = new int[8];
        imageURI[0]= R.drawable.upc;
        imageURI[1]= R.drawable.android;
        imageURI[2]= R.drawable.blender;
        imageURI[3]= R.drawable.fib;
        imageURI[4]= R.drawable.gimp;
        imageURI[5]= R.drawable.oasi;
        imageURI[6]= R.drawable.unity;
        imageURI[7]= R.drawable.apple;

        tScore = (TextView)findViewById(R.id.textViewScore);
        currentView = R.id.activityMemory;
        coolImageFlipper = new CoolImageFlipper(getApplicationContext());

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        users = new Users(getApplicationContext());

        initRandomImage();

        setImageViews();
    }

    public void initRandomImage() {
        randomImage = new ArrayList<Integer>(16);
        for(int i=0; i<16; i++) {
            randomImage.add(i/2);
        }
        Collections.shuffle(randomImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                flipCard(i1, 1);
                break;
            case R.id.imageView2:
                flipCard(i2, 2);
                break;
            case R.id.imageView3:
                flipCard(i3, 3);
                break;
            case R.id.imageView4:
                flipCard(i4, 4);
                break;
            case R.id.imageView5:
                flipCard(i5, 5);
                break;
            case R.id.imageView6:
                flipCard(i6, 6);
                break;
            case R.id.imageView7:
                flipCard(i7, 7);
                break;
            case R.id.imageView8:
                flipCard(i8, 8);
                break;
            case R.id.imageView9:
                flipCard(i9, 9);
                break;
            case R.id.imageView10:
                flipCard(i10, 10);
                break;
            case R.id.imageView11:
                flipCard(i11, 11);
                break;
            case R.id.imageView12:
                flipCard(i12, 12);
                break;
            case R.id.imageView13:
                flipCard(i13, 13);
                break;
            case R.id.imageView14:
                flipCard(i14, 14);
                break;
            case R.id.imageView15:
                flipCard(i15, 15);
                break;
            case R.id.imageView16:
                flipCard(i16, 16);
                break;
        }
    }

    public void setImageViews() {
        i1 = (ImageView) findViewById(R.id.imageView);
        i1.setOnClickListener(this);
        i2 = (ImageView) findViewById(R.id.imageView2);
        i2.setOnClickListener(this);
        i3 = (ImageView) findViewById(R.id.imageView3);
        i3.setOnClickListener(this);
        i4 = (ImageView) findViewById(R.id.imageView4);
        i4.setOnClickListener(this);
        i5 = (ImageView) findViewById(R.id.imageView5);
        i5.setOnClickListener(this);
        i6 = (ImageView) findViewById(R.id.imageView6);
        i6.setOnClickListener(this);
        i7 = (ImageView) findViewById(R.id.imageView7);
        i7.setOnClickListener(this);
        i8 = (ImageView) findViewById(R.id.imageView8);
        i8.setOnClickListener(this);
        i9 = (ImageView) findViewById(R.id.imageView9);
        i9.setOnClickListener(this);
        i10 = (ImageView) findViewById(R.id.imageView10);
        i10.setOnClickListener(this);
        i11 = (ImageView) findViewById(R.id.imageView11);
        i11.setOnClickListener(this);
        i12 = (ImageView) findViewById(R.id.imageView12);
        i12.setOnClickListener(this);
        i13 = (ImageView) findViewById(R.id.imageView13);
        i13.setOnClickListener(this);
        i14 = (ImageView) findViewById(R.id.imageView14);
        i14.setOnClickListener(this);
        i15 = (ImageView) findViewById(R.id.imageView15);
        i15.setOnClickListener(this);
        i16 = (ImageView) findViewById(R.id.imageView16);
        i16.setOnClickListener(this);
    }

    public void flipCard(final ImageView image, int i){
        int numImage = randomImage.get(i-1); //temporal
        coolImageFlipper.flipImage(getResources().getDrawable(imageURI[numImage]), image);
        if(flipping_second) {
            score++;
            flipping_second = false;
            tScore.setText("Score: " + Integer.toString(score));
            //wait 2 seconds
            if (numImage == pBefore) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            left--;
                            coolImageFlipper.flipImage(getResources().getDrawable(blank), image);
                            coolImageFlipper.flipImage(getResources().getDrawable(blank), iBefore);
                            if(left==0) {
                                memoryEnd();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
            else {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            left--;
                            coolImageFlipper.flipImage(getResources().getDrawable(backimage), image);
                            coolImageFlipper.flipImage(getResources().getDrawable(backimage), iBefore);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                coolImageFlipper.flipImage(getResources().getDrawable(backimage), image);
                coolImageFlipper.flipImage(getResources().getDrawable(backimage), iBefore);
            }
        }
        else {
            flipping_second = true;
            iBefore = image;
            pBefore = numImage;
        }

    }

    public void memoryEnd() {
        ContentValues valuesToStore = new ContentValues();
        valuesToStore.put("name", settings.getString("user", "No name"));
        valuesToStore.put("score", score);
        users.createScore(valuesToStore);

        AlertDialog.Builder builder = new AlertDialog.Builder(MemoryActivity.this);

        builder.setTitle("Final");
        builder.setMessage("Has terminado el memory");


        builder.setPositiveButton("Reiniciar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), MemoryActivity.class));
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
