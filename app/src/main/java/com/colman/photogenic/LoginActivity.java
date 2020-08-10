package com.colman.photogenic;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colman.photogenic.ui.user_profile.UserProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button loginButton;
    TextView tvSignUp;
    ImageView imageView;
    private UserProfileViewModel viewModel;
    private ProgressBar pgsBar;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        emailId = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);
        tvSignUp = findViewById(R.id.tvSignUp);
        imageView = findViewById(R.id.logo_image);
        pgsBar = findViewById(R.id.progress_circular);


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pgsBar.setVisibility(v.VISIBLE);
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();

                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    viewModel.login(email,pwd,data -> {
                        if(data)
                        {
                            Intent intToHome = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intToHome);
                            finish();
                            pgsBar.setVisibility(v.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            pgsBar.setVisibility(v.INVISIBLE);
                        }
                    });

                }
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                    pgsBar.setVisibility(v.INVISIBLE);

                }
            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intSignUp);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
