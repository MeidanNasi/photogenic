package com.colman.photogenic.ui.photos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.R;
import com.colman.photogenic.model.Photo;
import com.colman.photogenic.ui.user_profile.UserProfileViewModel;
import com.colman.photogenic.viewmodel.EditPhotoViewModel;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPhotoFragment extends Fragment {

    View root;
    NavController nav;
    private Photo r;
    private Map<String, Object> data;
    private final int PICK_IMAGE_REQUEST = 71;
    private EditText et_photo_name,et_photo_description;
    private ImageView img_photo;
    private ProgressBar progressBar;
    private EditPhotoViewModel viewModel;
    private UserProfileViewModel userProfileViewModel;

    private Uri filePath;
    int count = 0;
    boolean isPickedImage = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EditPhotoViewModel.class);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        r = PhotoDetailsFragmentArgs.fromBundle(getArguments()).getPhotoObj();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        et_photo_name = (EditText)view.findViewById(R.id.edit_photo_name);
        et_photo_description = (EditText)view.findViewById(R.id.edit_photo_description);


        img_photo = view.findViewById(R.id.img_photo);
        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        et_photo_name.setText(r.getName());
        et_photo_description.setText(r.getDescription());


        if(!r.getImgURL().equals(""))
        {
            Picasso.get().load(r.getImgURL()).into(img_photo, new com.squareup.picasso.Callback(){

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    if(isAdded())
                    {
                        img_photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                    }

                }
            });
        }
        else
        {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img_photo);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_edit_photo, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_button:
                if (count == 0)
                {
                    count++;
                    viewModel.deletePhoto(r, result -> {
                        nav.navigate(R.id.action_navigation_edit_photo_to_navigation_my_photos);
                        progressBar.setVisibility(item.getActionView().INVISIBLE);
                        count=0;
                    });
                }
                else{
                    Toast.makeText(getActivity(),"In Progress...",Toast.LENGTH_SHORT).show();
                    break;
                }

                return true;
            case R.id.save_button:
                if (count == 0)
                {
                    count++;
                    progressBar.setVisibility(item.getActionView().VISIBLE);
                    Photo photo = new Photo(r.getId(),et_photo_name.getText().toString(),r.getImgURL(), "", r.getCategory(),et_photo_description.getText().toString(),new Date());
                    userProfileViewModel.getCurrentUserEmail(email -> {
                        if(!email.isEmpty())
                        {
                            photo.setCreatedBy(email);
                        }
                    });
                    if(isPickedImage)
                    {
                        viewModel.uploadImage(filePath,result -> {
                            //data.put("imgURL",result.toString());
                            photo.setImgURL(result.toString());
                            viewModel.updatePhoto(photo,result2 -> {
                                if(result2)
                                {
                                    nav.navigate(R.id.action_navigation_edit_photo_to_navigation_my_photos);
                                    progressBar.setVisibility(item.getActionView().INVISIBLE);
                                    count=0;
                                }
                            });
                        });

                    }
                    else
                    {
                        viewModel.updatePhoto(photo,result ->{
                            if(result)
                            {
                                nav.navigate(R.id.action_navigation_edit_photo_to_navigation_my_photos);
                                progressBar.setVisibility(item.getActionView().INVISIBLE);
                                count=0;
                            }
                        });

                    }
                    isPickedImage = false;
                }
                else{
                    Toast.makeText(getActivity(),"In Progress...",Toast.LENGTH_SHORT).show();
                    break;
                }
                isPickedImage = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
            Picasso.get().load(filePath).into(img_photo);
            isPickedImage = true;

        }
    }
}
