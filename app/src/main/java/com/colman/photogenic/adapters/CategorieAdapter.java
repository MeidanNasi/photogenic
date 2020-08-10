package com.colman.photogenic.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.colman.photogenic.R;
import com.colman.photogenic.RecyclerViewClickInterface;
import com.colman.photogenic.model.Category;
import java.util.ArrayList;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>
{
    private ArrayList<Category> dataList;
    private RecyclerViewClickInterface recyclerViewClickInterface;



    public CategorieAdapter(ArrayList<Category> dataList)
    {
        this.dataList = dataList;
    }
    public CategorieAdapter(ArrayList<Category> dataList, RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.dataList = dataList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public CategorieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_categorie, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategorieViewHolder holder, int position)
    {
        String categoryPressed = dataList.get(position).getName();
        Resources res = holder.itemView.getContext().getResources();
        holder.txtCategorieName.setText(categoryPressed);
        switch(categoryPressed){
            case "Animals":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.animals));
                break;
            case "Decor":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.decor));
                break;
            case "Nature":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.nature));
                break;
            case "Food":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.food));
                break;
            case "Sport":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.sports));
                break;
            case "Views":
                holder.img.setImageDrawable(res.getDrawable(R.drawable.views));
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    /////////////////////////
    /// View Holder Class ///
    /////////////////////////

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class CategorieViewHolder extends RecyclerView.ViewHolder
    {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private int mCurrentPosition;
        ImageView img;
        TextView txtCategorieName;
        CardView myCardView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public CategorieViewHolder(View itemView)
        {
            super(itemView);
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            img = (ImageView) itemView.findViewById(R.id.imageView);
            txtCategorieName = (TextView) itemView.findViewById(R.id.txt_categorie_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

        }

    }


}
