package com.colman.photogenic.ui.user_profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.colman.photogenic.LoginActivity;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.R;
import com.squareup.picasso.Picasso;
public class UserProfileFragment extends Fragment {

    private UserProfileViewModel userProfileViewModel;
    Button btnLogout;
    private TextView txt_user_email;
    private final int PICK_IMAGE_REQUEST = 71;
    private ProgressBar progressBar;
    private ImageView img_user;
    private Uri filePath;
    View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Profile");

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // Get the specific view
        root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        txt_user_email = (TextView)view.findViewById(R.id.txt_user_email);
        userProfileViewModel.getCurrentUserEmail( email -> {
            txt_user_email.setText(email);
        });
        img_user = (ImageView)view.findViewById(R.id.profile_image);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // Logout button
        btnLogout = (Button) view.findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(v.VISIBLE);
                userProfileViewModel.logout();
                Intent intToMain = new Intent(getActivity(), LoginActivity.class);
                startActivity(intToMain);
                getActivity().finish();
                progressBar.setVisibility(v.INVISIBLE);
            }

        });
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Fix when not picking picture
        if(requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Picasso.get().load(filePath).into(img_user);
        }
    }


}
