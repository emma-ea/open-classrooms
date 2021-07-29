package com.oddlycoder.ocr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.oddlycoder.ocr.R;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeCallbacks,
        ProfileFragment.ProfileCallbacks {

    public static final String TAG = "MainActivity";
    public static final String AUTH_CONTEXT = "auth-context";

    private CoordinatorLayout mParentLayout;

    private int exitCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            homeFragment();
        }

        mParentLayout = findViewById(R.id.main_activity);

        BottomNavigationView mBottomNavView = findViewById(R.id.bottom_nav_bar);
        mBottomNavView.setOnNavigationItemSelectedListener(navListener);

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
                //TODO: disabled booked feature, t.o.l
            // TODO: uncomment item in nav menu and here to reinstate
            /*case R.id.booked_menu:
                bookedFragment();
                break;*/
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

    public void bookedFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, BookedListFragment.class, null)
                .commit();
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public static Intent start(Context ctx) {
        authActivity = (AuthActivity) ctx;
        return new Intent(ctx, MainActivity.class);
    }

    private static AuthActivity authActivity;

    @Override
    public void logout() {
        // authActivity.googleSignOut();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void startAuthActivity() {
        startActivity(AuthActivity.start(this));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        authActivity = null;
    }

    @Override
    public void deleteProfile() {
        authActivity.googleSignOut();
        startAuthActivity();
    }
}
