package com.bhavaneulergmail.baking.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavaneulergmail.baking.Adapters.IngredientsAdapter;
import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.Ingredients;
import com.bhavaneulergmail.baking.WidgetDataProvider;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by prajwalm on 18/09/17.
 */

public class IngredientFragment extends Fragment{


    private int mPosition ;
    private String LOG_TAG = IngredientFragment.class.getSimpleName();
    @BindView(R.id.ingredient_recycler_view) RecyclerView ingredientRecyclerView;
    @BindView(R.id.title_recipe)TextView mTitle;
    public static String mRecipeName;
    RecyclerView.LayoutManager layoutManager;
    private LoaderManager.LoaderCallbacks<String> titleLoader= new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<String>(getContext()) {


                @Override
                public void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public String loadInBackground() {
                    String  title = null;
                    try {
                        String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                        title = JsonParsing.getRecipeName(jsonResponse, mPosition);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "EXCEPTION:-" + e, e);
                    }
                    return title;
                }
            };

        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {

            mTitle.setText(data);
            mRecipeName=data;

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<ArrayList<Ingredients>> ingredientLoader = new LoaderManager.LoaderCallbacks<ArrayList<Ingredients>>(){


        @Override
        public Loader<ArrayList<Ingredients>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Ingredients>>(getContext()) {


                @Override
                public void onStartLoading(){
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public ArrayList<Ingredients> loadInBackground() {

                    ArrayList<Ingredients> ingredientsArrayList = null;

                    try {
                        String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                        ingredientsArrayList= JsonParsing.getIngredients(jsonResponse,mPosition);
                    }catch (IOException e){
                        Log.e(LOG_TAG,"EXCEPTION:-"+e,e);
                    }
                    return ingredientsArrayList;
                }
            };        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Ingredients>> loader, ArrayList<Ingredients> data) {

            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(),data);
            ingredientRecyclerView.setAdapter(ingredientsAdapter);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Ingredients>> loader) {

        }
    };


    @Override

    public View onCreateView(LayoutInflater layoutInflater , ViewGroup container , Bundle savedInstanceState){

        View rootView;
        if(checkNetwork()) {
            rootView = layoutInflater.inflate(R.layout.ingredient_fragment, container, false);
            ButterKnife.bind(this,rootView);
            mPosition = getActivity().getIntent().getIntExtra(NameFragment.POSITION, 0);
           // ingredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.ingredient_recycler_view);
            layoutManager = new LinearLayoutManager(getContext());
            ingredientRecyclerView.setLayoutManager(layoutManager);
            //mButton=rootView.findViewById(R.id.fav_but);
            getLoaderManager().initLoader(1,null,titleLoader);
            getLoaderManager().initLoader(0, null, ingredientLoader);
            setHasOptionsMenu(true);


         /*   mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WidgetDataProvider.mIndexNumber=mPosition;
                    Toast.makeText(getContext()," Widget Updated To Show Current Recipe Ingredients",Toast.LENGTH_SHORT).show();
                }
            });*/

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.widget_menu, menu);
        return;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){

        int id= menuItem.getItemId();
        if(id==R.id.widget_menu){
             WidgetDataProvider.mIndexNumber=mPosition;
            Toast.makeText(getContext()," Widget Updated To Show Current Recipe Ingredients",Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(menuItem);


    }



}



