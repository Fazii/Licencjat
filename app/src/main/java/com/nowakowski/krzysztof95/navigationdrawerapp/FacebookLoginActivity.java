package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

        prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_id", loginResult.getAccessToken().getUserId());
                editor.apply();

                Intent refActivity = new Intent(FacebookLoginActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Zalogowno", Toast.LENGTH_LONG).show();

                startActivity(refActivity);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                if (currentProfile != null) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user", currentProfile.getName());
                    editor.putString("user_picture",currentProfile.getProfilePictureUri(150, 150).toString());
                    editor.putBoolean("valid", true);
                    editor.apply();

                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("user_id");
                    editor.remove("user");
                    editor.remove("user_picture");
                    editor.apply();
                    Intent refActivity = new Intent(FacebookLoginActivity.this, MainActivity.class);
                    startActivity(refActivity);
                    Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
