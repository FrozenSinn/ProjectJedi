package com.example.cram_.projectjedi;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    protected int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setView();
    }

    protected void setView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_nav, R.string.close_nav);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activityCalc:
                if(currentView != R.id.activityProfile) finish();
                startActivity(new Intent(getApplicationContext(),CalculadoraActivity.class));
                break;
            case R.id.activityMemory:
                if(currentView != R.id.activityProfile) finish();
                startActivity(new Intent(getApplicationContext(),MemoryActivity.class));
                break;
            case R.id.activityRanking:
                if(currentView != R.id.activityProfile) finish();
                startActivity(new Intent(getApplicationContext(),RankingActivity.class));
                break;
            case R.id.activityProfile:
                if(currentView != R.id.activityProfile) finish();
                break;
            case R.id.activityMPlayer:
                if(currentView != R.id.activityProfile) finish();
                startActivity(new Intent(getApplicationContext(),MediaPlayerActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void setContentView(int layoutResID) {

        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.frame_layout_base);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullLayout);
        setView();
    }
}
