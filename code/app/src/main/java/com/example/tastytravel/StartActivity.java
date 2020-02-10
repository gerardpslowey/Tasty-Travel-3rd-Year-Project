package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    TextView continueText;
    FirebaseAuth mAuth;
    Button signInBtn;
    TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Continue without account option
        continueText = findViewById(R.id.continueWithoutAccount);

        signInBtn = findViewById(R.id.signInBtn);
        mAuth = FirebaseAuth.getInstance();
        signUpText = findViewById(R.id.signUpText);

        TextView signUpText = findViewById(R.id.signUpText);
        Spannable word = new SpannableString("Don't have an account?");
        word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(word);
        Spannable wordTwo = new SpannableString(" SIGN UP");
        wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.aqua_blue)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.append(wordTwo);

        // checking if user is already logged in
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // Continue without account
        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notLoggedIn_mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(notLoggedIn_mainScreen);
            }
        });

        // Sign In
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInScreen = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInScreen);
            }
        });

        // Sign Up
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpScreen = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpScreen);
            }
        });
    }
}