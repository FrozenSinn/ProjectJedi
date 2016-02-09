package com.example.cram_.projectjedi;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class RankingActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private ArrayList<String> usersList;
    private ArrayList<Integer> scoreList;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        currentView = R.id.activityRanking;

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
}
