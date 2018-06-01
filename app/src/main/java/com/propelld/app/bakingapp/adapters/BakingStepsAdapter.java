package com.propelld.app.bakingapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Step;
import com.propelld.app.bakingapp.tasks.TaskListener;
import com.propelld.app.bakingapp.tasks.ThumbnailTask;
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
    private TaskListener taskListener;
    private final int NOVIDEO = 0;
    private final int VIDEO = 1;

    public BakingStepsAdapter(Context context,
                              ArrayList<Step> steps,
                              OnStepClick onStepClick,
                              TaskListener taskListener)
    {
        this.context = context;
        this.steps = steps;
        this.onStepClick = onStepClick;
        this.taskListener = taskListener;
    }

    public  class BakingStepsNoVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtShortDescription;
        ImageView imageView;

        public BakingStepsNoVideoViewHolder(View view)
        {
            super(view);
            txtShortDescription = (TextView) view.findViewById(R.id.txt_step_shortDescription);
            imageView = (ImageView) view.findViewById(R.id.step_imageView);
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

    private void bindWithNoVideoViewHolder(final BakingStepsNoVideoViewHolder holder,
                                           int position)
    {
        final Step step = steps.get(position);
        holder.txtShortDescription.setText(step.getShortDescription());
        if (!StringUtils.isNullOrWhiteSpace(step.getThumbnailURL()))
        {
            holder.imageView.setVisibility(View.VISIBLE);

            if (step.getThumbnail() != null)
            {
                holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(
                        step.getThumbnail(), 96, 96, false));
            }
            else
            {
                new ThumbnailTask(holder.imageView,
                        context,
                        MediaStore.Video.Thumbnails.MICRO_KIND,
                        taskListener,
                        step)
                        .execute(step.getThumbnailURL());
            }
        }
    }
}