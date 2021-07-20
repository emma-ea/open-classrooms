package com.oddlycoder.ocr.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.utils.FirebaseUserSetup;
import com.oddlycoder.ocr.utils.IGoogleSignOut;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.viewmodel.AuthViewModel;

public class AuthActivity extends AppCompatActivity implements IGoogleSignOut {

    public static final String TAG = "AuthActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    private GoogleSignInOptions gso;

    private ConstraintLayout mParentLayout;
    private ProgressBar mProgressCircle;
    private Button mAuthButton;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        mParentLayout = findViewById(R.id.auth_parent_layout);
        mProgressCircle = findViewById(R.id.auth_progress_circular);
        mProgressCircle.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        // configuring auth api
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // client[user] require auth setup
        mSignInClient = GoogleSignIn.getClient(this, gso);

        mAuthButton = findViewById(R.id.auth_login_button);
        mAuthButton.setOnClickListener(buttonListener);

    }

    private final View.OnClickListener buttonListener = action -> {
        signIn();
        Log.i(TAG, "Button pressed: Auth");
    };


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        startMainActivity(user);
    }

    private void showButton() {
        mAuthButton.setVisibility(View.VISIBLE);
        mProgressCircle.setVisibility(View.GONE);
    }

    private void hideButton() {
        mAuthButton.setVisibility(View.INVISIBLE);
        mProgressCircle.setVisibility(View.VISIBLE);
    }

    public void startMainActivity(FirebaseUser user) {
        if (user != null) {
            startActivity(MainActivity.start(AuthActivity.this));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            // TODO finish activity
            // hideButton();
            finish();
        } else {
            snackMessage("");  // handle with internal message
            showButton();
        }
    }

    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        // startActivityForResult(signInIntent, RC_SIGN_IN);
        startGoogleAuthIntent.launch(signInIntent);
        //TODO: not working as expected. fix error
        hideButton();
    }

    private final ActivityResultLauncher<Intent> startGoogleAuthIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> {
                if (o.getResultCode() == RESULT_OK) {
                    Intent data = o.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            Log.d(TAG, "onActivityResult: " + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        }
                    } catch (ApiException e) {
                        Log.w(TAG, "onActivityResult: ", e);
                    }
                } else {
                    Log.d(TAG, "Couldn't start goog auth: ");
                    snackMessage("Couldn't start authentication. Please try again");
                    showButton();
                }
            }
    );

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isComplete()) {
                        Log.d(TAG, "signInWithCredential: Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        snackMessage("Authentication successful, Please wait");
                        if (user != null) {
                            Student student = new Student(
                                    user.getPhotoUrl().toString(),
                                    user.getDisplayName(),
                                    "",
                                    "",
                                    "",
                                    user.getEmail()
                            );
                            Log.d(TAG, "firebaseAuthWithGoogle: user not null. creating user");
                            new FirebaseUserSetup(idToken, student);
                        }
                        startMainActivity(user);
                    } else {
                        Log.w(TAG, "signInWithCredential: Failure", task.getException());
                        // auth failed
                        snackMessage("");
                        startMainActivity(null);
                    }
                    showButton();
                });
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void googleSignOut() {
        // disconnect app user google account
        GoogleSignIn.getClient(this, gso).signOut()
                .addOnCompleteListener((listener) -> {
                    mAuth.signOut();
                    Log.d(TAG, "googleSignOut: user account disconnected.");
                });
    }


}
