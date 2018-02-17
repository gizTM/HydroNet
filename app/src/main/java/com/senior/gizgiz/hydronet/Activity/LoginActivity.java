package com.senior.gizgiz.hydronet.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;

import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.Fragment.LoginFragment;
import com.senior.gizgiz.hydronet.Fragment.SignupFragment;
import com.senior.gizgiz.hydronet.R;

public class LoginActivity extends AppCompatActivity {

    private Fragment f;
    private CustomTextView login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_tab);
        signup = findViewById(R.id.signup_tab);

        login.setStyle("Bold");
        signup.setStyle("Regular");

        f = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_fragment_container, f);
        ft.commit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setStyle("Bold");
                signup.setStyle("Regular");

                f = new LoginFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_fragment_container, f);
                ft.commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setStyle("Regular");
                signup.setStyle("Bold");

                f = new SignupFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_fragment_container, f);
                ft.commit();
            }
        });
    }
}
