package com.colman.photogenic;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.colman.photogenic.ui.user_profile.UserProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity{

    AppBarConfiguration appBarConfiguration; // config settings for navigation bar
    public static ProgressBar mainProgressBar; // ux for client
    public static Context context; // wtf ?
    private UserProfileViewModel userProfileViewModel; // for checking if client is authenticated



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        userProfileViewModel.isUserLoggedIn(data -> {
            if(!data)
            {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mainProgressBar = findViewById(R.id.progressBar);
        context = getApplicationContext();

        // nav bar creation
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_categories,R.id.navigation_my_photos, R.id.navigation_user_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
    }





}
