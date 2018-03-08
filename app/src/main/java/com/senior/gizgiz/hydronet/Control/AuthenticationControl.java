package com.senior.gizgiz.hydronet.Control;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Admins on 005 05/03/2018.
 */

public class AuthenticationControl {
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public static FirebaseUser getCurrentUser() { return currentUser; }
    public static String getCurrentUsername() { return currentUser.getDisplayName(); }
    public static String getCurrentUserEmail() { return currentUser.getEmail(); }
    public static boolean isCurrentUserEmailVerified() { return currentUser.isEmailVerified(); }
}
