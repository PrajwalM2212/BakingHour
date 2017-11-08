package com.bhavaneulergmail.baking;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {


    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mPlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
    }



    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }


}
