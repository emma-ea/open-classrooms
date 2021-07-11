package com.oddlycoder.ocr.views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.repo.Repository;
import com.oddlycoder.ocr.utils.NetworkConnection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "com.oddlycoder.ocr";

    private CoordinatorLayout mParentLayout;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    private int exitCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            homeFragment();
        }

        mParentLayout = findViewById(R.id.main_activity);

        BottomNavigationView mBottomNavView = findViewById(R.id.bottom_nav_bar);
        mBottomNavView.setOnNavigationItemSelectedListener(navListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        //TODO seems not to work, fix later
       /* if (!Repository.initialize(this).getNetworkState()) {
            snackMessage(getString(R.string.poor_net_connection_msg));
        } else {
            Log.d(TAG, "MainActivity: started: network connection available");
        }*/

    }

    @Override
    public void onBackPressed() {
        if (exitCount == 0)
            snackMessage("Press back button again to exit app");
        if (exitCount == 1)
            snackMessage("Press back button one more time to exit");
        if (exitCount >= 2)
            super.onBackPressed();
        exitCount++;
        // reset exitCount after 2 seconds if user has pressed back button.
        Executors.newSingleThreadScheduledExecutor()
                .schedule(()-> exitCount = 0, 2000L, TimeUnit.MILLISECONDS);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int id = item.getItemId();
        switch (id) {
            case R.id.home_menu:
                homeFragment();
                break;
            case R.id.search_menu:
                searchFragment();
                break;
            case R.id.profile_menu:
                profileFragment();
                break;
        }
        return true;
    };

    public void homeFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, HomeFragment.class, null)
                .commit();
        Log.d(TAG, "homeFragment: ");
    }

    public void searchFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, SearchFragment.class, null)
                .commit();
        Log.d(TAG, "searchFragment: ");
    }

    public void profileFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, ProfileFragment.class, null)
                .commit();
        Log.d(TAG, "profileFragment: ");
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public static Intent start(Context ctx) { return new Intent(ctx, MainActivity.class); }
}
