package com.bhavaneulergmail.baking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.Description;

import java.util.ArrayList;

/**
 * Created by prajwalm on 18/09/17.
 */

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Description> descriptionArrayList;
    private ClickInterface mClickInterface;

    public interface ClickInterface{
        void onClick(int position);
    }





    public DescriptionAdapter(Context context , ArrayList<Description> descriptionArrayList, ClickInterface clickInterface){

        this.context=context;
        this.descriptionArrayList=descriptionArrayList;
        this.mClickInterface=clickInterface;


    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Description description = descriptionArrayList.get(position);
        TextView descriptView = holder.descriptionView;
        descriptView.setText(description.description);

    }

    @Override
    public int getItemCount() {
        if(descriptionArrayList==null){
            return 0;
        }else {
            return descriptionArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descriptionView ;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            descriptionView =itemView.findViewById(R.id.step_description);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickInterface.onClick(position);

        }
    }

}
