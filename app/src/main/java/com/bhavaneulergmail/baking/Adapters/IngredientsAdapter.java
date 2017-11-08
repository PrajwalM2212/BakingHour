package com.bhavaneulergmail.baking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.Ingredients;

import java.util.ArrayList;

/**
 * Created by prajwalm on 17/09/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Ingredients> mIngredientsArrayList;


    public IngredientsAdapter(Context context , ArrayList<Ingredients> ingredientsArrayList ){
       this.mContext=context;
       this.mIngredientsArrayList=ingredientsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Ingredients ingredients = mIngredientsArrayList.get(position);

        TextView quantity = holder.quantityView;
        TextView measure = holder.measureView;
        TextView ingredient = holder.ingredientView;


        quantity.setText("Quantity : "+ingredients.quantity);
        measure.setText("Measure : "+ingredients.measure);
        ingredient.setText("Ingredient : "+ingredients.ingredient);


    }

    @Override
    public int getItemCount() {
        if(mIngredientsArrayList==null){
        return 0;}
        else{
            return  mIngredientsArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView quantityView;
        TextView measureView;
        TextView ingredientView;
        public ViewHolder(View itemView) {
            super(itemView);
            quantityView =itemView.findViewById(R.id.quantity);
            measureView=itemView.findViewById(R.id.measure);
            ingredientView=itemView.findViewById(R.id.ingredient);
        }
    }

}
