package com.propelld.app.bakingapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.propelld.app.bakingapp.MainActivity;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Step;
import com.propelld.app.bakingapp.tasks.ThumbnailTask;
import com.propelld.app.bakingapp.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment
{
    private final String STEP_SAVED_INSTANCE = "STEP_SAVED_INSTANCE";
    private final String POSITION_SAVED = "POSITION_SAVED";
    private final String STATE_SAVED = "STATE_SAVED";
    private final String BITMAP_IMAGE = "BITMAP_IMAGE";
    private final String APPLICATION_NAME = "BAKING_APPLICATION";
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ImageView imageView;
    private Step step;
    private long currentVideoPosition;
    private boolean currentIsPlaying = true;
    private Bitmap bitmapImage;

    public DescriptionFragment()
    {
    }

    public void setStep(Step step)
    {
        this.step = step;
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        saveState();
        state.putSerializable(STEP_SAVED_INSTANCE, step);
        state.putLong(POSITION_SAVED, Math.max(0, currentVideoPosition));
        state.putBoolean(STATE_SAVED, currentIsPlaying);

        Bitmap bitmapImage = null;

        if (imageView.getDrawable() != null)
        {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            bitmapImage = drawable.getBitmap();
        }
        state.putParcelable(BITMAP_IMAGE, bitmapImage);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (Util.SDK_INT <= 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (Util.SDK_INT > 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (Util.SDK_INT > 23)
        {
            String url = !StringUtils.isNullOrWhiteSpace(step.getVideoURL())
                    ? step.getVideoURL()
                    : null;

            if (!StringUtils.isNullOrWhiteSpace(url))
            {
                Uri uri = Uri.parse(url);
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(uri);
            }
            else
            {
                simpleExoPlayerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if ((Util.SDK_INT <= 23 || simpleExoPlayer == null))
        {
            String url = !StringUtils.isNullOrWhiteSpace(step.getVideoURL())
                    ? step.getVideoURL()
                    : null;

            if (!StringUtils.isNullOrWhiteSpace(url))
            {
                Uri uri = Uri.parse(url);
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(uri);
            }
            else
            {
                simpleExoPlayerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater
                .inflate(R.layout.fragment_description, container, false);
        TextView descTextView = (TextView) rootView.findViewById(R.id.desc_text);
        imageView = (ImageView) rootView.findViewById(R.id.desc_imageView);

        if (savedInstanceState != null &&
                (savedInstanceState.getSerializable(STEP_SAVED_INSTANCE) != null))
        {
            step = (Step)savedInstanceState.getSerializable(STEP_SAVED_INSTANCE);
            currentVideoPosition = savedInstanceState.getLong(POSITION_SAVED);
            currentIsPlaying = savedInstanceState.getBoolean(STATE_SAVED);
            bitmapImage = savedInstanceState.getParcelable(BITMAP_IMAGE);
        }

        ((AppCompatActivity)getActivity()).setTitle(step.getShortDescription());

        if (!StringUtils.isNullOrWhiteSpace(step.getThumbnailURL()))
        {
            imageView.setVisibility(View.VISIBLE);
            if (bitmapImage == null)
            {
                new ThumbnailTask(imageView,
                        getContext(),
                        MediaStore.Video.Thumbnails.MINI_KIND,
                        (MainActivity)getActivity(),
                        step)
                        .execute(step.getThumbnailURL());
            }
            else
            {
                imageView.setImageBitmap(bitmapImage);
            }
        }
        else
        {
            imageView.setVisibility(View.GONE);
        }

        descTextView.setText(step.getDescription());

        simpleExoPlayerView = new SimpleExoPlayerView(getContext());
        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.des_video);
        simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.novideo));

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
            simpleExoPlayer.seekTo(currentVideoPosition);
            simpleExoPlayer.setPlayWhenReady(currentIsPlaying);
        }
    }

    private  void releasePlayer()
    {
        if (simpleExoPlayer != null)
        {
            currentVideoPosition = simpleExoPlayer.getCurrentPosition();
            currentIsPlaying = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void saveState()
    {
        if (simpleExoPlayer != null)
        {
            currentVideoPosition = simpleExoPlayer.getCurrentPosition();
            currentIsPlaying = simpleExoPlayer.getPlayWhenReady();
        }
    }
}