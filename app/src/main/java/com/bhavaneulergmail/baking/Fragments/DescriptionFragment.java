package com.bhavaneulergmail.baking.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bhavaneulergmail.baking.Adapters.DescriptionAdapter;
import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.StepActivity;
import com.bhavaneulergmail.baking.Templates.Description;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prajwalm on 18/09/17.
 */

public class DescriptionFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Description>>,DescriptionAdapter.ClickInterface{

    private int mPosition;
    private static String LOG_TAG = DescriptionFragment.class.getSimpleName();
    @BindView(R.id.description_recycler_view) RecyclerView descriptionRecyclerView;
    public static final String STEP_POSITION="step_position";
    private RecyclerView.LayoutManager layoutManager;
    private Parcelable mListState;
    private String KEY="SAVE";





    @Override
    public View onCreateView(LayoutInflater layoutInflater , ViewGroup container , Bundle savedInstanceState){



       View rootView;

        if(checkNetwork()) {
            rootView = layoutInflater.inflate(R.layout.fragment_description, container, false);

            ButterKnife.bind(this,rootView);
            mPosition = getActivity().getIntent().getIntExtra(NameFragment.POSITION, 0);
            //descriptionRecyclerView = (RecyclerView) rootView.findViewById(R.id.description_recycler_view);
            layoutManager=new LinearLayoutManager(getContext());
            descriptionRecyclerView.setLayoutManager(layoutManager);
            getLoaderManager().initLoader(11, null, this);

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
    public Loader<ArrayList<Description>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Description>>(getContext()) {

            @Override

            public void onStartLoading(){

              super.onStartLoading();
              forceLoad();

            }
            @Override
            public ArrayList<Description> loadInBackground() {
                ArrayList<Description>descriptionArrayList = new ArrayList<>();
               try{
                   String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                   descriptionArrayList = JsonParsing.getDescription(jsonResponse,mPosition);

               }catch (IOException e){
                   Log.e(LOG_TAG,"Exception:-"+e,e);
               }

               return descriptionArrayList;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Description>> loader, ArrayList<Description> data) {

        DescriptionAdapter descriptionAdapter = new DescriptionAdapter(getContext(),data,this);
        descriptionRecyclerView.setAdapter(descriptionAdapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Description>> loader) {

    }

    @Override
    public void onClick(int position) {
        Toast.makeText(getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(),StepActivity.class);
        intent.putExtra(STEP_POSITION,position);
        intent.putExtra(NameFragment.POSITION,mPosition);
        startActivity(intent);
    }



}
