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

public class MainActivity extends AppCompatActivity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "uw0UmKPfHah6t7dBGMc8OJWUN";
    private static final String TWITTER_SECRET = "Ep5jS459QW1X6S94thjKlkV9InNo13fOJ3x16Fauolez9O5Kfl";

    EditText tName, tPass;
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
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        tName = (EditText) findViewById(R.id.edTName);
        tPass = (EditText) findViewById(R.id.edTPass);
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
                } else if (!settings.getBoolean("logged", false)) {
                    ContentValues valuesToStore = new ContentValues();
                    valuesToStore.put("name", String.valueOf(session.getUserName()));
                    valuesToStore.put("pass", "twitter");
                    valuesToStore.put("image", "default");
                    valuesToStore.put("notif", "alert");
                    if (users.createUser(valuesToStore)) {
                        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("logged", true);
                        editor.putString("user", session.getUserName());
                        editor.apply();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), FailActivity.class);
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(getApplicationContext(), FailActivity.class);
            startActivity(intent);
        }
    }

    public void register(View view) {
        ContentValues valuesToStore = new ContentValues();
        valuesToStore.put("name", String.valueOf(tName.getText()));
        valuesToStore.put("pass", String.valueOf(tPass.getText()));
        valuesToStore.put("image", "default");
        valuesToStore.put("notif", "alert");
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
}
