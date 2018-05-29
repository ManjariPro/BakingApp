package com.propelld.app.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.models.Baking;
import java.util.ArrayList;

/**
 * Created by manjari on 27/5/18.
 */

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.BakingViewHolder>
{
    private ArrayList<Baking> bakings;
    private Context context;
    private OnBakingRecipeClick onBakingRecipeClick;

    public BakingAdapter(ArrayList<Baking> bakings,
                         Context context,
                         OnBakingRecipeClick onBakingRecipeClick)
    {
        this.bakings = bakings;
        this.context = context;
        this.onBakingRecipeClick = onBakingRecipeClick;
    }

    public  class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtRecipe;

        public BakingViewHolder(View view)
        {
            super(view);
            txtRecipe = (TextView) view.findViewById(R.id.receipeName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int i = getAdapterPosition();
            Baking baking = bakings.get(i);

            // Now pass this baking to the Activity
            onBakingRecipeClick.onBakingRecipeClick(baking);
        }
    }

    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachParentImmediately = false;

        View view = inflater.inflate(R.layout.reciperow, parent, shouldAttachParentImmediately);
        BakingViewHolder bakingViewHolder = new BakingViewHolder(view);

        return bakingViewHolder;
    }

    @Override
    public void onBindViewHolder(BakingViewHolder holder, int position)
    {
        Baking baking = bakings.get(position);
        holder.txtRecipe.setText(baking.getName());
    }

    @Override
    public int getItemCount()
    {
        return bakings.size();
    }
}