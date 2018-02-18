package com.senior.gizgiz.hydronet.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.MicrogearConsoleActivity;
import com.senior.gizgiz.hydronet.R;

public class LoginFragment extends Fragment {
    private EditText usernameET, passwordET;
    private Button loginBT, fbBT, ggBT;
    private ProgressDialog loginProgress;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameET = view.findViewById(R.id.username_text);
        passwordET = view.findViewById(R.id.password_text);

        loginBT = view.findViewById(R.id.login_button);
        fbBT = view.findViewById(R.id.login_fb_button);
        ggBT = view.findViewById(R.id.login_gg_button);

        fbBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MicrogearConsoleActivity.class));
            }
        });
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        loginProgress = new ProgressDialog(getContext());
    }

    private void login () {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please enter username", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        loginProgress.setMessage("Logging in...");
        loginProgress.show();

        startActivity(new Intent(getContext(), MainActivity.class));
    }
}
