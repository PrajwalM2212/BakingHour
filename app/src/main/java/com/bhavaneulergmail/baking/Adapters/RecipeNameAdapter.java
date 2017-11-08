package com.bhavaneulergmail.baking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.RecipesNames;

import java.util.ArrayList;

/**
 * Created by prajwalm on 17/09/17.
 */

public class RecipeNameAdapter extends RecyclerView.Adapter<RecipeNameAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<RecipesNames> mRecipesNamesArrayList;
    private ClickInterface mClickInterface;

    public interface ClickInterface{
        void onClick(int position);
    }



    public RecipeNameAdapter( Context context ,ArrayList<RecipesNames> recipesNamesArrayList ,ClickInterface clickInterface ){
        this.mContext=context;
        this.mRecipesNamesArrayList=recipesNamesArrayList;
        this.mClickInterface = clickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_name_item,parent,false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RecipesNames recipesNames = mRecipesNamesArrayList.get(position);

        TextView nameTextView = holder.nameText;
        nameTextView.setText(recipesNames.recipeName);

    }

    @Override
    public int getItemCount() {

        if(mRecipesNamesArrayList==null) {
            return 0;
        }else{
            return mRecipesNamesArrayList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView nameText;
        public ViewHolder(View itemView) {
            super(itemView);
            nameText =(TextView)itemView.findViewById(R.id.name_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickInterface.onClick(position);
        }
    }
}
