package com.example.cram_.projectjedi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class RankingActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private ArrayList<String> usersList;
    private ArrayList<Integer> scoreList;
    SharedPreferences settings;
    public static final String PREFS_NAME = "prefs";
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mLinearLayout = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayout);

        users = new Users(getApplicationContext());

        usersList = new ArrayList<String>();
        scoreList = new ArrayList<Integer>();
        Cursor c = users.getScores();
        if (c.moveToFirst()) {
            do {
                usersList.add(c.getString(c.getColumnIndex("name")));
                scoreList.add(c.getInt(c.getColumnIndex("score")));
            } while (c.moveToNext());
        }

        mRecyclerView.setAdapter(new MyCustomAdapter(usersList, scoreList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout_memory:
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();

                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.reset:
                users.resetScores();
                intent = new Intent(getApplicationContext(), RankingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
