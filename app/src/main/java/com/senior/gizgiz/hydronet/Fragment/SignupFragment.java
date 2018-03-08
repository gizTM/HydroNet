package com.senior.gizgiz.hydronet.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.senior.gizgiz.hydronet.Entity.User;
import com.senior.gizgiz.hydronet.HelperClass.AESCrypt;
import com.senior.gizgiz.hydronet.HelperClass.CustomButton;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;

public class SignupFragment extends Fragment {
    private CustomEditText usernameET,emailET,passwordET,confirmPWET;
    private CustomButton signupButton;
    private ProgressDialog signupProgress;

    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        usernameET = view.findViewById(R.id.username_text);
        emailET = view.findViewById(R.id.email_text);
        passwordET = view.findViewById(R.id.password_text);
        confirmPWET = view.findViewById(R.id.confirm_pw_text);
        signupButton = view.findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        signupProgress = new ProgressDialog(getContext());
    }

    private void register() {
        final String username = usernameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        String confirmPass = confirmPWET.getText().toString().trim();
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please enter username", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_LONG).show();
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

        signupProgress.setMessage("Signing up user...");
        signupProgress.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
//                    alert("SignUp successful!");
                    Toast.makeText(getActivity(), "Signed up successfully", Toast.LENGTH_LONG).show();
                    String encryptedPass = "";
                    try {
                        encryptedPass = AESCrypt.encrypt(password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RealTimeDBManager.setUpdateValueEventListener();
                    final String finalEncryptedPass = encryptedPass;
                    RealTimeDBManager.getDatabase().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            RealTimeDBManager.writeNewUser(username,email, finalEncryptedPass);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    startActivity(new Intent(getContext(),MainActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Could not sign up .. Please check input field and try again...", Toast.LENGTH_LONG).show();
                }
                signupProgress.dismiss();
            }
        });
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
