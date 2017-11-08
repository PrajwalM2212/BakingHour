package com.bhavaneulergmail.baking;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavaneulergmail.baking.Adapters.DescriptionAdapter;
import com.bhavaneulergmail.baking.Adapters.IngredientsAdapter;
import com.bhavaneulergmail.baking.Fragments.DescriptionFragment;
import com.bhavaneulergmail.baking.Fragments.IngredientFragment;
import com.bhavaneulergmail.baking.Fragments.NameFragment;
import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.Templates.Description;
import com.bhavaneulergmail.baking.Templates.Ingredients;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements DescriptionAdapter.ClickInterface {


    private boolean mTwoPane;
    private int mPosition;
    @BindView(R.id.ingredient_recycler_view) RecyclerView ingredientRecyclerView;
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    @BindView(R.id.description_recycler_view) RecyclerView descriptionRecyclerView;
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.steps) TextView mSteps;
    @BindView(R.id.fav)Button mFavourite;
    @BindView(R.id.recipe_image)ImageView bakeImage;
    private int mStepPosition;



    private LoaderManager.LoaderCallbacks<StepDetails> recipeDetailsLoader = new LoaderManager.LoaderCallbacks<StepDetails>() {
        @Override
        public Loader<StepDetails> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<StepDetails>(getBaseContext()) {

                @Override
                public void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public StepDetails loadInBackground() {
                    StepDetails stepDetails = null;
                    try {
                        String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));

                        stepDetails = JsonParsing.getRecipeDetails(jsonResponse, mPosition, mStepPosition);


                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Exception" + e, e);

                    }

                    return stepDetails;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<StepDetails> loader, StepDetails stepDetails) {

            if(!stepDetails.mediaUrl.isEmpty()) {
                mPlayerView.setVisibility(View.VISIBLE);
                bakeImage.setVisibility(View.INVISIBLE);
                initializePlayer(stepDetails.mediaUrl);

            }else{

                mPlayerView.setVisibility(View.INVISIBLE);
                bakeImage.setVisibility(View.VISIBLE);
                try {
                    Picasso.with(DetailsActivity.this).load(stepDetails.imageUrl).into(bakeImage);
                }catch (IllegalArgumentException e){
                    Log.e(LOG_TAG,"Exception:-"+e,e);
                    Picasso.with(DetailsActivity.this).load(R.drawable.bake).into(bakeImage);
                }

                if(bakeImage.getDrawable()==null){
                    Picasso.with(DetailsActivity.this).load(R.drawable.bake).into(bakeImage);
                }




            }
            mSteps.setText(stepDetails.stepDescription);


        }

        @Override
        public void onLoaderReset(Loader<StepDetails> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Ingredients>> ingredientsLoader
            = new LoaderManager.LoaderCallbacks<ArrayList<Ingredients>>() {

        @Override
        public Loader<ArrayList<Ingredients>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Ingredients>>(getBaseContext()) {


                @Override
                public void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public ArrayList<Ingredients> loadInBackground() {

                    ArrayList<Ingredients> ingredientsArrayList = null;

                    try {
                        String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                        ingredientsArrayList = JsonParsing.getIngredients(jsonResponse, mPosition);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "EXCEPTION:-" + e, e);
                    }
                    return ingredientsArrayList;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Ingredients>> loader, ArrayList<Ingredients> data) {

            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getBaseContext(), data);
            ingredientRecyclerView.setAdapter(ingredientsAdapter);
            mFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WidgetDataProvider.mIndexNumber=mPosition;
                    Toast.makeText(DetailsActivity.this," Widget Updated To Show Current Recipe Ingredients",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Ingredients>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Description>> descriptionsLoader
            = new LoaderManager.LoaderCallbacks<ArrayList<Description>>() {
        @Override
        public Loader<ArrayList<Description>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Description>>(getBaseContext()) {

                @Override

                public void onStartLoading() {

                    super.onStartLoading();
                    forceLoad();

                }

                @Override
                public ArrayList<Description> loadInBackground() {
                    ArrayList<Description> descriptionArrayList = new ArrayList<>();
                    try {
                        String jsonResponse = JsonOutput.makeHttpRequest(getString(R.string.query_string));
                        descriptionArrayList = JsonParsing.getDescription(jsonResponse, mPosition);

                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Exception:-" + e, e);
                    }

                    return descriptionArrayList;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Description>> loader, ArrayList<Description> data) {

            DescriptionAdapter descriptionAdapter = new DescriptionAdapter(getBaseContext(), data, getInterface());
            descriptionRecyclerView.setAdapter(descriptionAdapter);

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Description>> loader) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        if (findViewById(R.id.tab_layout) != null) {
            mTwoPane = true;
        }

        if (mTwoPane) {
            ButterKnife.bind(this);


            mPosition = getIntent().getIntExtra(NameFragment.POSITION, 0);
            //ingredientRecyclerView = (RecyclerView) findViewById(R.id.ingredient_recycler_view);
            ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            //descriptionRecyclerView = (RecyclerView) findViewById(R.id.description_recycler_view);
            descriptionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            //mPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.bake));
            mSteps = (TextView) findViewById(R.id.steps);

            LoaderManager  loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(111, null, ingredientsLoader);
            loaderManager.initLoader(121, null, descriptionsLoader);
            loaderManager.initLoader(131,null,recipeDetailsLoader);


        }


    }


    private DescriptionAdapter.ClickInterface getInterface() {
        return this;
    }

    @Override
    public void onClick(int position) {
        mStepPosition = position;
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            releasePlayer();
        }
        LoaderManager loaderManager =getSupportLoaderManager();
        loaderManager.restartLoader(131, null, recipeDetailsLoader);

    }


    private void initializePlayer(String mediaUrl) {

        Uri mediaUri = Uri.parse(mediaUrl);

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this,
                    userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }

    }


    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            releasePlayer();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        if(mExoPlayer!=null && mPlayerView.getVisibility()==View.VISIBLE) {
            releasePlayer();
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
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }




}