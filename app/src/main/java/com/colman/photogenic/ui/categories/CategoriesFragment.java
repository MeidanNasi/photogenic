package com.colman.photogenic.ui.categories;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.colman.photogenic.MainActivity;
import com.colman.photogenic.R;
import com.colman.photogenic.RecyclerViewClickInterface;
import com.colman.photogenic.adapters.CategorieAdapter;
import com.colman.photogenic.model.Category;
import java.util.ArrayList;



public class CategoriesFragment extends Fragment implements RecyclerViewClickInterface {

    View root;
    NavController nav;
    ArrayList<Category> categoriesList =new ArrayList<>();
    private RecyclerView recyclerView;
    private CategorieAdapter adapter;



    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nav = NavHostFragment.findNavController(this);
        MainActivity.mainProgressBar.setVisibility(view.INVISIBLE);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.mainProgressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Categories");
        categoriesList = new ArrayList<Category>() {
            {
                add(new Category("Nature"));
                add(new Category("Animals"));
                add(new Category("Views"));
                add(new Category("Sport"));
                add(new Category("Decor"));
                add(new Category("Food"));
            }
        };

        root = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new CategorieAdapter(categoriesList, this);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onItemClick(int position) {

        CategoriesFragmentDirections.ActionNavigationCategoriesToNavigationPhotosList action = CategoriesFragmentDirections.actionNavigationCategoriesToNavigationPhotosList(categoriesList.get(position).name);
        action.setCategory(categoriesList.get(position).name);
        nav.navigate(action);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void onBackPressed()
    {
        // Disable back button

    }

}
