package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class notLoggedIn_MainActivity extends AppCompatActivity {

    Button searchBtn;
    Button settingsBtn;
    Button profileButton;
    Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SearchActivity.class);
                // pass info to 2nd screen
                startActivity(startIntent);
            }
        });

        settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                // pass info to 2nd screen
                startActivity(settingsIntent);
            }
        });

        profileButton = findViewById(R.id.profileBtn);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInIntent);
            }
        });

        // About
        aboutBtn = findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aboutScreen = new Intent(getApplicationContext(), AboutActivity.class);
                // pass info to 2nd screen
                startActivity(aboutScreen);
            }
        });
    }
}