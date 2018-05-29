package com.propelld.app.bakingapp.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Step;
import com.propelld.app.bakingapp.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment
{
    private final String STEP_SAVED_INSTANCE = "STEP_SAVED_INSTANCE";
    private final String APPLICATION_NAME = "BAKING_APPLICATION";
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Step step;

    public DescriptionFragment()
    {
        // Required empty public constructor
    }

    public void setStep(Step step)
    {
        this.step = step;
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putSerializable(STEP_SAVED_INSTANCE, step);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater
                .inflate(R.layout.fragment_description, container, false);
        TextView descTextView = (TextView) rootView.findViewById(R.id.desc_text);

        if (savedInstanceState != null &&
                (savedInstanceState.getSerializable(STEP_SAVED_INSTANCE) != null))
        {
            step = (Step)savedInstanceState.getSerializable(STEP_SAVED_INSTANCE);
        }

        descTextView.setText(step.getDescription());

        simpleExoPlayerView = new SimpleExoPlayerView(getContext());
        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.des_video);
        simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.novideo));

        String url = !StringUtils.isNullOrWhiteSpace(step.getVideoURL())
                ? step.getVideoURL()
                : step.getThumbnailURL();

        if (!StringUtils.isNullOrWhiteSpace(url))
        {
            Uri uri = Uri.parse(url);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            releasePlayer();
            initializePlayer(uri);
        }
        else
        {
            simpleExoPlayerView.setVisibility(View.GONE);
        }
        return rootView;
    }

    private void initializePlayer(Uri mediaUri)
    {
        if (simpleExoPlayer == null)
        {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), APPLICATION_NAME);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private  void releasePlayer()
    {
        if (simpleExoPlayer != null)
        {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }
}