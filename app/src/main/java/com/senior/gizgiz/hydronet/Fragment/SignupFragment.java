package com.senior.gizgiz.hydronet.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.senior.gizgiz.hydronet.CustomHelperClass.CustomButton;
import com.senior.gizgiz.hydronet.MainActivity;
import com.senior.gizgiz.hydronet.R;

public class SignupFragment extends Fragment {

    private SpannableString hintUsername,hintEmail,hintPassword,hintConfirmPW;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private EditText usernameET,emailET,passwordET,confirmPWET;
    private CustomButton signupButton;
    private ProgressDialog signupProgress;

//    private FirebaseAuth firebaseAuth;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        firebaseAuth = FirebaseAuth.getInstance();

        // set font for hint text
        hintUsername = new SpannableString("Username");
        hintEmail = new SpannableString("Email");
        hintPassword = new SpannableString("Password");
        hintConfirmPW = new SpannableString("Confirm password");
        hintUsername.setSpan(MainActivity.regularSpan, 0, hintUsername.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        hintEmail.setSpan(MainActivity.regularSpan, 0, hintEmail.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        hintPassword.setSpan(MainActivity.regularSpan, 0, hintPassword.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        hintConfirmPW.setSpan(MainActivity.regularSpan, 0, hintConfirmPW.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        usernameET = (EditText) view.findViewById(R.id.username_text);
        emailET = (EditText) view.findViewById(R.id.email_text);
        passwordET = (EditText) view.findViewById(R.id.password_text);
        confirmPWET = (EditText) view.findViewById(R.id.confirm_pw_text);
        usernameET.setHint(hintUsername);
        emailET.setHint(hintEmail);
        passwordET.setHint(hintPassword);
        confirmPWET.setHint(hintConfirmPW);

        signupButton = (CustomButton) view.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        signupProgress = new ProgressDialog(getContext());
    }

    private void register() {
        String username = usernameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPass = confirmPWET.getText().toString().trim();
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please enter username", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter ic_email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(getContext(), "Please confirm password", Toast.LENGTH_LONG).show();
            return;
        }

        signupProgress.setMessage("Signing up ic_user...");
        signupProgress.show();
/*
        firebaseAuth.createUserWithEmailAndPassword(ic_email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
//                    alert();
                    Toast.makeText(getActivity(), "Signed up successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Could not sign up .. Please try again...", Toast.LENGTH_LONG).show();
                }
                signupProgress.dismiss();
            }
        });
        */
    }

    void alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(msg);
        alertDialog.setMessage("db updated"+"\ncheck firebase web");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
