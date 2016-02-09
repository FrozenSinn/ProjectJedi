package com.example.cram_.projectjedi;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "uw0UmKPfHah6t7dBGMc8OJWUN";
    private static final String TWITTER_SECRET = "Ep5jS459QW1X6S94thjKlkV9InNo13fOJ3x16Fauolez9O5Kfl";


    Button b, b2, bUsers;
    EditText tName, tPass;
    RelativeLayout rlay;
    Users users;

    public static final String PREFS_NAME = "prefs";

    SharedPreferences settings;
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isLogged = settings.getBoolean("logged", false);
        if(isLogged) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            finish();
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        bUsers = (Button) findViewById(R.id.buttonUsers);
        bUsers.setOnClickListener(this);
        tName = (EditText) findViewById(R.id.edTName);
        tPass = (EditText) findViewById(R.id.edTPass);
        rlay = (RelativeLayout) findViewById(R.id.RelativeLayout);
        users = new Users(getApplicationContext());

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                Cursor c = users.getPassByName(session.getUserName());
                String password = "";
                if (c.moveToFirst()) {
                    password = c.getString(c.getColumnIndex("pass"));
                    if (password.equals("twitter")) {
                        Toast.makeText(getApplicationContext(), "Logged", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("logged", true);
                        editor.putString("user", session.getUserName());
                        editor.apply();
                    }
                }
                else if(!settings.getBoolean("logged", false)) {
                    ContentValues valuesToStore = new ContentValues();
                    valuesToStore.put("name", String.valueOf(session.getUserName()));
                    valuesToStore.put("pass", String.valueOf("twitter"));
                    if(users.createUser(valuesToStore)) {
                        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("logged", true);
                        editor.putString("user", session.getUserName());
                        editor.apply();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                finish();
                startActivity(intent);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    public void login(View view) {
        Cursor c = users.getPassByName(tName.getText().toString());
        String password = "";
        if (c.moveToFirst()) {
            password = c.getString(c.getColumnIndex("pass"));
            if (password.equals(tPass.getText().toString())&& !password.equals("twitter")) {
                Toast.makeText(getApplicationContext(), "Logged", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged", true);
                editor.putString("user", tName.getText().toString());
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                finish();
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Wrong user", Toast.LENGTH_SHORT).show();
    }

    public void register(View view) {
        ContentValues valuesToStore = new ContentValues();
        valuesToStore.put("name", String.valueOf(tName.getText()));
        valuesToStore.put("pass", String.valueOf(tPass.getText()));
        if(users.createUser(valuesToStore)) {
            Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
            tName.setText("");
            tPass.setText("");
        }
        else
            Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Snackbar snackbar = Snackbar.make(rlay, "Soy un snackbar!", Snackbar.LENGTH_SHORT);

                snackbar.show();
                break;
            case R.id.button2:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Error");
                builder.setMessage("Archivo no encontrado");

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                            }
                        }
                );

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                            }
                        }
                );

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case R.id.buttonUsers:
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(intent);
        }
    }
}
