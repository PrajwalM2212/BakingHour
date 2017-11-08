package com.bhavaneulergmail.baking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bhavaneulergmail.baking.Adapters.RecipeNameAdapter;
import com.bhavaneulergmail.baking.DetailsActivity;
import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.RecipesNames;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prajwalm on 17/09/17.
 */

public class NameFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<RecipesNames>>,RecipeNameAdapter.ClickInterface {


    @BindView(R.id.name_recycler_view) RecyclerView nameRecyclerView;
    public static final String POSITION="clickPosition";

    @Override
    public View onCreateView(LayoutInflater layoutInflater , ViewGroup container , Bundle savedInstanceState){

        View rootView;

        if(checkNetwork()) {
            rootView = layoutInflater.inflate(R.layout.name_fragment, container, false);

            ButterKnife.bind(this,rootView);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float width = displayMetrics.widthPixels / displayMetrics.density;
            float heigth = displayMetrics.heightPixels / displayMetrics.density;

            //nameRecyclerView = (RecyclerView) rootView.findViewById(R.id.name_recycler_view);
            if (width > 640 && heigth > 480) {
                nameRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                nameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            LoaderManager loaderManager = getLoaderManager();


            loaderManager.initLoader(0, null, this);

        }else{
            rootView = layoutInflater.inflate(R.layout.empty_layout, container, false);

        }


        return rootView;



    }

    public boolean checkNetwork(){

        ConnectivityManager connectivityManager =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }


    @Override
    public Loader<ArrayList<RecipesNames>> onCreateLoader(int i, Bundle bundle) {


        return new android.support.v4.content.AsyncTaskLoader<ArrayList<RecipesNames>>(getContext()) {

            ArrayList<RecipesNames> mNames;

            @Override
            public void onStartLoading(){

                super.onStartLoading();



                if(mNames!=null){
                    deliverResult(mNames);
                }else {

                    forceLoad();
                }


            }
            @Override
            public ArrayList<RecipesNames> loadInBackground() {
                ArrayList<RecipesNames> recipesNames = null;
                try{

                    String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                    recipesNames = JsonParsing.getRecipesNames(jsonResponse);

                }catch (IOException e){
                    e.printStackTrace();
                }

                return recipesNames;
            }


            @Override
            public void deliverResult(ArrayList<RecipesNames> recipesNames){

                mNames=recipesNames;
                super.deliverResult(recipesNames);

            }


        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RecipesNames>> loader, ArrayList<RecipesNames> recipesNames) {

        if(recipesNames!=null) {
            RecipeNameAdapter nameAdapter = new RecipeNameAdapter(getContext(), recipesNames,this);
            nameRecyclerView.setAdapter(nameAdapter);
        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipesNames>> loader) {

    }


    @Override
    public void onClick(int position) {
        Toast.makeText(getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(POSITION,position);
        startActivity(intent);

    }

}
