package com.bhavaneulergmail.baking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.R;
import com.bhavaneulergmail.baking.Templates.StepDetails;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prajwalm on 19/09/17.
 */




public class RecipeDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<StepDetails> {

    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView  mPlayerView;
    private static String LOG_TAG=RecipeDetailsFragment.class.getSimpleName();
    private int mPosition;
    private int mStepPosition;
    @BindView(R.id.steps) TextView mSteps;
    @BindView(R.id.next_button) Button mNextButton;
    private int mCount=0;
    private int mStepCount;
    @BindView(R.id.previous_button) Button mPreviousButton;
    @BindView(R.id.recipe_image) ImageView bakeImage;



    public View onCreateView(LayoutInflater layoutInflater , ViewGroup container ,Bundle savedInstanceState){



        View rootView;
        if(checkNetwork()) {
            rootView = layoutInflater.inflate(R.layout.fragment_recipe_details, container, false);

            ButterKnife.bind(this,rootView);


            //mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exo_player_view);
           // mSteps = (TextView) rootView.findViewById(R.id.steps);
            //mNextButton = (Button) rootView.findViewById(R.id.next_button);
            //mPreviousButton = (Button) rootView.findViewById(R.id.previous_button);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.bake));


            Intent intent = getActivity().getIntent();

            mStepPosition = intent.getIntExtra(DescriptionFragment.STEP_POSITION, 0);

            mPosition = intent.getIntExtra(NameFragment.POSITION, 0);


            getLoaderManager().initLoader(22, null, this);


            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCount + mStepPosition < mStepCount - 1) {
                        mCount = mCount + 1;
                        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
                            releasePlayer();
                        }
                        load();

                    } else {
                        mCount = -mStepPosition;
                        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
                            releasePlayer();
                        }
                        load();
                    }
                }
            });


            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mStepPosition + mCount > 0) {
                        mCount = mCount - 1;
                        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
                            releasePlayer();
                        }
                        load();
                    } else {
                        mCount = -mStepPosition + mStepCount - 1;
                        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
                            releasePlayer();
                        }
                        load();
                    }
                }
            });

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


    private void load(){
        getLoaderManager().restartLoader(22,null,this);
    }


    private void initializePlayer(String mediaUrl){

        Uri mediaUri = Uri.parse(mediaUrl);

        if(mExoPlayer==null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(),
                    userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }

    }

    private void releasePlayer(){
       mExoPlayer.stop();
       mExoPlayer.release();
       mExoPlayer = null;

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            releasePlayer();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop(){
        super.onStop();
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            releasePlayer();
        }
    }


    @Override
    public Loader<StepDetails> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<StepDetails>(getContext()) {

            @Override
            public void onStartLoading(){
              super.onStartLoading();
              forceLoad();
            }
            @Override
            public StepDetails loadInBackground() {
                StepDetails stepDetails=null;
                try{
                    String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));

                     stepDetails = JsonParsing.getRecipeDetails(jsonResponse, mPosition, mStepPosition+mCount);


                }catch (IOException e){
                   Log.e(LOG_TAG,"Exception"+e,e);

                }

                return stepDetails;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<StepDetails> loader, final StepDetails stepDetails) {

        if(!stepDetails.mediaUrl.isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            bakeImage.setVisibility(View.INVISIBLE);
            initializePlayer(stepDetails.mediaUrl);

        }else{

            mPlayerView.setVisibility(View.INVISIBLE);
            bakeImage.setVisibility(View.VISIBLE);

           try {
                 Picasso.with(getContext()).load(stepDetails.imageUrl).into(bakeImage);
             }catch (IllegalArgumentException e){
                 Log.e(LOG_TAG,"Exception:-"+e,e);
                 Picasso.with(getContext()).load(R.drawable.bake).into(bakeImage);
             }

             if(bakeImage.getDrawable()==null){
                 Picasso.with(getContext()).load(R.drawable.bake).into(bakeImage);
             }


        }
            mSteps.setText(stepDetails.stepDescription);



            new Task().execute();




    }

    @Override
    public void onLoaderReset(Loader<StepDetails> loader) {

    }


   public class Task extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {

            int arrayCount=0;
            try{
                String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));

                arrayCount=JsonParsing.getArrayCount(jsonResponse,mPosition);


            }catch (IOException e){
                Log.e(LOG_TAG,"Exception"+e,e);

            }

            return arrayCount;
        }



        @Override
        protected void onPostExecute(Integer arrayCount){

            mStepCount=arrayCount;



        }

    }




}



