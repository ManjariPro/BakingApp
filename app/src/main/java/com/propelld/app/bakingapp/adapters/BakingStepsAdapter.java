package com.propelld.app.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Step;
import com.propelld.app.bakingapp.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by manjari on 27/5/18.
 */

public class BakingStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<Step> steps;
    private Context context;
    private OnStepClick onStepClick;
    private final int NOVIDEO = 0;
    private final int VIDEO = 1;

    public BakingStepsAdapter(Context context,
                              ArrayList<Step> steps,
                              OnStepClick onStepClick)
    {
        this.context = context;
        this.steps = steps;
        this.onStepClick = onStepClick;
    }

    public  class BakingStepsNoVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtShortDescription;

        public BakingStepsNoVideoViewHolder(View view)
        {
            super(view);
            txtShortDescription = (TextView) view.findViewById(R.id.txt_step_shortDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int i = getAdapterPosition();
            Step step = steps.get(i);
            onStepClick.onStepClick(step);
        }
    }

    public  class BakingStepsWithVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtShortDescription;
        ImageButton videoImageButton;

        public BakingStepsWithVideoViewHolder(View view)
        {
            super(view);
            txtShortDescription = (TextView) view.findViewById(R.id.txt_video_step_shortDescription);
            videoImageButton = (ImageButton) view.findViewById(R.id.btn_video_img);
            videoImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int i = getAdapterPosition();
            Step step = steps.get(i);
            onStepClick.onStepClick(step);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Step step = steps.get(position);

        if (StringUtils.isNullOrWhiteSpace(step.getVideoURL()))
        {
            return NOVIDEO;
        }
        else
        {
            return VIDEO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachParentImmediately = false;

        switch (viewType)
        {
            case NOVIDEO:
                View noVideoView = inflater.inflate(R.layout.steprow, parent, shouldAttachParentImmediately);
                viewHolder = new BakingStepsNoVideoViewHolder(noVideoView);
                break;
            case VIDEO:
                View videoView = inflater.inflate(R.layout.steprowvideo, parent, shouldAttachParentImmediately);
                viewHolder = new BakingStepsWithVideoViewHolder(videoView);
                break;
            default:
                View view = inflater.inflate(R.layout.steprow, parent, shouldAttachParentImmediately);
                viewHolder = new BakingStepsNoVideoViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case NOVIDEO:
                bindWithNoVideoViewHolder((BakingStepsNoVideoViewHolder)holder, position);
                break;
            case VIDEO:
                bindWithVideoViewHolder((BakingStepsWithVideoViewHolder)holder, position);
                break;
            default:
                bindWithNoVideoViewHolder((BakingStepsNoVideoViewHolder)holder, position);
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return steps.size();
    }

    private void bindWithVideoViewHolder(BakingStepsWithVideoViewHolder holder,
                                         int position)
    {
        Step step = steps.get(position);
        holder.txtShortDescription.setText(step.getShortDescription());
    }

    private void bindWithNoVideoViewHolder(BakingStepsNoVideoViewHolder holder,
                                           int position)
    {
        Step step = steps.get(position);
        holder.txtShortDescription.setText(step.getShortDescription());
    }
}