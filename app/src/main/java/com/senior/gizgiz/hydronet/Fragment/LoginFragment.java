package com.senior.gizgiz.hydronet.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.MicrogearConsoleActivity;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.R;


public class LoginFragment extends Fragment {
    private EditText usernameET, passwordET;
    private Button loginBT, fbBT, ggBT;
    private ProgressDialog loginProgress;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
//                loginWithFB();
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
        final String username = usernameET.getText().toString().trim();
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

        firebaseAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(currentUser != null) {
                                Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                currentUser.updateProfile(profileUpdates);
                                String uid = currentUser.getUid();
                                String token = FirebaseInstanceId.getInstance().getToken();
                                RealTimeDBManager.getDatabase().child("users/"+uid+"/notificationTokens").setValue(null);
                                RealTimeDBManager.getDatabase().child("users/"+uid+"/notificationTokens/"+token).setValue(true);
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        } else Toast.makeText(getActivity(), "Could not log in .. Please check and try again...", Toast.LENGTH_LONG).show();
                        loginProgress.dismiss();
                    }
                });
//        startActivity(new Intent(getContext(), MainActivity.class));
    }
    private void loginWithFB() {

    }
}
