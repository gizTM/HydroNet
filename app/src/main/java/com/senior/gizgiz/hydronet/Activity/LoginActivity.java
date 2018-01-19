package com.senior.gizgiz.hydronet.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;

import com.senior.gizgiz.hydronet.CustomHelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Fragment.LoginFragment;
import com.senior.gizgiz.hydronet.Fragment.SignupFragment;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.R;

public class LoginActivity extends AppCompatActivity {

    private Fragment f;
    private SpannableString login_tab,signup_tab;
    private CustomTextView login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (CustomTextView) findViewById(R.id.login_tab);
        signup = (CustomTextView) findViewById(R.id.signup_tab);

        login_tab = new SpannableString("Log In");
        login_tab.setSpan(MainActivity.boldSpan, 0, login_tab.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        login.setText(login_tab);

        f = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment_container, f);
        ft.commit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_tab = new SpannableString("Log In");
                login_tab.setSpan(MainActivity.boldSpan, 0, login_tab.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                login.setText(login_tab);
                signup_tab = new SpannableString("Sign Up");
                signup_tab.setSpan(MainActivity.regularSpan, 0, signup_tab.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                signup.setText(signup_tab);

                f = new LoginFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_fragment_container, f);
                ft.commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_tab = new SpannableString("Sign Up");
                signup_tab.setSpan(MainActivity.boldSpan, 0, login_tab.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                signup.setText(signup_tab);
                login_tab = new SpannableString("Log In");
                login_tab.setSpan(MainActivity.regularSpan, 0, login_tab.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                login.setText(login_tab);

                f = new SignupFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_fragment_container, f);
                ft.commit();
            }
        });
    }
}
