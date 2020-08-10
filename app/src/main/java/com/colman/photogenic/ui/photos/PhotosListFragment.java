package com.colman.photogenic.ui.photos;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.R;
import com.colman.photogenic.RecyclerViewClickInterface;
import com.colman.photogenic.model.Photo;
import com.colman.photogenic.model.PhotoModel;
import com.colman.photogenic.viewmodel.PhotoListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhotosListFragment extends Fragment implements RecyclerViewClickInterface {

    public static NavController nav;
    FloatingActionButton fab;
    private TextView tv_no_data;
    private PhotoListViewModel viewModel;
    View root;
    public static List<Photo> photosList =new ArrayList<>();
    private String pressed_category;
    private ProgressBar progressBar;
    LiveData<List<Photo>> liveData;
    RecyclerView list;
    public static List<Photo> data = new LinkedList<Photo>();
    NewPhotosListAdapter new_adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PhotoListViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_photos_list, container, false);
        list = root.findViewById(R.id.recycler_view);
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        new_adapter = new NewPhotosListAdapter();
        list.setAdapter(new_adapter);

        progressBar = root.findViewById(R.id.progressBar);
        tv_no_data = root.findViewById(R.id.tv_no_data);
        pressed_category = PhotosListFragmentArgs.fromBundle(getArguments()).getCategory();

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if(!photos.isEmpty())
                {
                    tv_no_data.setVisibility(View.INVISIBLE);
                    data = photos;
                    data.removeIf(photo -> (photo.isDeleted() || !photo.getCategory().equals(pressed_category)));
                    new_adapter.notifyDataSetChanged();
                }
                else
                {
                    tv_no_data.setVisibility(View.VISIBLE);
                }
                MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);
            }

        });

        final SwipeRefreshLayout swipeRefresh = root.findViewById(R.id.photos_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new PhotoModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


        ((MainActivity) getActivity()).getSupportActionBar().setTitle(pressed_category);


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.mainProgressBar.setVisibility(view.VISIBLE);

        // Floating action button
        nav = NavHostFragment.findNavController(this);
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotosListFragmentDirections.ActionNavigationPhotosListToNavigationNewPhoto action = PhotosListFragmentDirections.actionNavigationPhotosListToNavigationNewPhoto(pressed_category);
                action.setMessage(pressed_category);
                nav.navigate(action);

            }
        });
    }


    @Override
    public void onItemClick(int position) {

        Photo r = data.get(position);
        PhotosListFragmentDirections.ActionNavigationPhotosListToNavigationPhotoDetails action = PhotosListFragmentDirections.actionNavigationPhotosListToNavigationPhotoDetails(r);
        action.setPhotoObj(r);
        nav.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    static class PhotoRowViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPhotoName;
        TextView photoDescription;
        ImageView img;
        Photo photo;

        public PhotoRowViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            txtPhotoName = itemView.findViewById(R.id.txt_photo_name);
            photoDescription = itemView.findViewById(R.id.txt_photo_description);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Photo r = data.get(position);
                    PhotosListFragmentDirections.ActionNavigationPhotosListToNavigationPhotoDetails action = PhotosListFragmentDirections.actionNavigationPhotosListToNavigationPhotoDetails(r);
                    action.setPhotoObj(r);
                    nav.navigate(action);
                }
            });

        }

        public void bind(Photo r) {
            txtPhotoName.setText(r.name);
            photoDescription.setText(r.description);
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img);
            if(!r.getImgURL().equals(""))
            {
                Picasso.get().load(r.getImgURL()).into(img, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
            else
            {
                Picasso.get().load(R.drawable.ic_launcher_foreground).into(img);
            }
            photo = r;
        }
    }

    class NewPhotosListAdapter extends RecyclerView.Adapter<PhotoRowViewHolder>
    {
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


        @NonNull
        @Override
        public PhotoRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.row_photo, viewGroup,false );
            PhotoRowViewHolder vh = new PhotoRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoRowViewHolder studentRowViewHolder, int i) {
            Photo st = data.get(i);
            studentRowViewHolder.bind(st);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

}
