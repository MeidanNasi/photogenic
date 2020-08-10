package com.colman.photogenic.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {
    public static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser mFirebaseUser;


    public static void login(String email, String password,final UserModel.Listener<Boolean> listener) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.onComplete(task.isSuccessful());
                mFirebaseUser = task.getResult().getUser();
            }
        });
        }

    public static void signUp(String email, String password, final UserModel.Listener<Boolean> listener) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.onComplete(task.isSuccessful());
                mFirebaseUser = task.getResult().getUser();
            }
        });
        }

    public static void logout() {
            mFirebaseAuth.signOut();
            mFirebaseUser = null;
        }

    public static void isUserLoggedIn(final UserModel.Listener<Boolean> listener) {
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if( mFirebaseUser != null )
            {
                listener.onComplete(true);
            }
            else
            {
                listener.onComplete(false);
            }
        }


    public static void getCurrentUserEmail(final UserModel.Listener<String> listener) {
        String email = mFirebaseAuth.getCurrentUser().getEmail();
            if( email != null )
            {
                listener.onComplete(email);
            }
        }
    }
