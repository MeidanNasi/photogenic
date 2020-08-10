package com.colman.photogenic.model;

public class UserModel {
    public static final UserModel instance = new UserModel();

    // Login
    // Register
    // Sign Up


    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private UserModel(){
    }

    public void login(String email, String password,Listener<Boolean> listener) {
        AuthFirebase.login(email,password,listener);
    }

    public void logout() {
        AuthFirebase.logout();
    }

    public void isUserLoggedIn(Listener<Boolean> listener) {
        AuthFirebase.isUserLoggedIn(listener);
    }

    public void signUp(String email, String password,Listener<Boolean> listener) {
        AuthFirebase.signUp(email,password,listener);
    }

    public void getCurrentUserEmail(Listener<String> listener){
        AuthFirebase.getCurrentUserEmail(listener);
    }


}
