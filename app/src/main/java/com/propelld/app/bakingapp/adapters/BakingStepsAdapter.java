package com.propelld.app.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Step;
import java.util.ArrayList;

/**
 * Created by manjari on 27/5/18.
 */

public class BakingStepsAdapter extends RecyclerView.Adapter<BakingStepsAdapter.BakingStepsViewHolder>
{
    ArrayList<Step> steps;
    private Context context;
    private OnStepClick onStepClick;

    public BakingStepsAdapter(Context context,
                              ArrayList<Step> steps,
                              OnStepClick onStepClick)
    {
        this.context = context;
        this.steps = steps;
        this.onStepClick = onStepClick;
    }

    public  class BakingStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtShortDescription;

        public BakingStepsViewHolder(View view)
        {
            super(view);
            txtShortDescription = (TextView) view.findViewById(R.id.step_shortDescription);
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

    @Override
    public BakingStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachParentImmediately = false;

        View view = inflater.inflate(R.layout.steprow, parent, shouldAttachParentImmediately);
        BakingStepsViewHolder bakingStepsViewHolder = new BakingStepsViewHolder(view);

        return bakingStepsViewHolder;
    }

    @Override
    public void onBindViewHolder(BakingStepsViewHolder holder, int position)
    {
        Step step = steps.get(position);
        holder.txtShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount()
    {
        return steps.size();
    }
}