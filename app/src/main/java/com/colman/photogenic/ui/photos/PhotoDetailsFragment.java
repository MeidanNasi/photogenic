package com.colman.photogenic.ui.photos;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.R;
import com.colman.photogenic.model.Photo;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDetailsFragment extends Fragment {

    View root;
    NavController nav;
    private Photo r;
    private TextView tv_photo_name;
    private TextView tv_photo_description;
    private ImageView img_photo;

    public PhotoDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_photo_details, container, false);
        r = PhotoDetailsFragmentArgs.fromBundle(getArguments()).getPhotoObj();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());
        MainActivity.mainProgressBar.setVisibility(View.VISIBLE);


        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        tv_photo_name = view.findViewById(R.id.txt_photo_name);
        tv_photo_description = view.findViewById(R.id.txt_photo_description);
        img_photo = view.findViewById(R.id.img_photo);


        tv_photo_name.setText(r.getName());
        tv_photo_description.setText(r.getDescription());


        if(!r.getImgURL().equals(""))
        {
            Picasso.get().load(r.getImgURL()).into(img_photo, new com.squareup.picasso.Callback(){

                @Override
                public void onSuccess() {
                    MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    if(isAdded())
                    {
                        img_photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                        MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);

                    }

                }
            });
        }
        else {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img_photo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getCategory());
    }


}
