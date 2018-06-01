package com.propelld.app.bakingapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.propelld.app.bakingapp.Configuration;
import com.propelld.app.bakingapp.intentService.BakingIntentService;
import com.propelld.app.bakingapp.MainActivity;
import com.propelld.app.bakingapp.R;
import com.propelld.app.bakingapp.adapters.BakingStepsAdapter;
import com.propelld.app.bakingapp.adapters.OnStepClick;
import com.propelld.app.bakingapp.models.Baking;
import com.propelld.app.bakingapp.models.Ingredient;
import com.propelld.app.bakingapp.tasks.TaskListener;
import java.util.ArrayList;

public class DetailViewFragment extends Fragment
{
    private final String BAKING_SAVED_INSTANCE = "STEP_SAVED_INSTANCE";
    private Baking baking;
    private OnStepClick onStepClick;

    public DetailViewFragment()
    {
    }

    public void setBaking(Baking baking)
    {
        this.baking = baking;
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putSerializable(BAKING_SAVED_INSTANCE, baking);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        onStepClick = (MainActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);

        TextView headingTextView = (TextView) rootView.findViewById(R.id.details_ingredientHeading);
        TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.details_ingredients);
        headingTextView.setText(R.string.Ingredients_Head);

        if (savedInstanceState != null &&
                (savedInstanceState.getSerializable(BAKING_SAVED_INSTANCE) != null))
        {
            baking = (Baking)savedInstanceState.getSerializable(BAKING_SAVED_INSTANCE);
        }
        ((AppCompatActivity)getActivity()).setTitle(baking.getName());

        ingredientsTextView.setText(getIngredients(baking.getIngredients()));

        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.details_steps);
        BakingStepsAdapter bakingStepsAdapter =
                new BakingStepsAdapter(getContext(), baking.getSteps(), onStepClick, (TaskListener) getActivity());
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));
        recyclerView.setAdapter(bakingStepsAdapter);

        SharedPreferences pref = getContext()
                .getApplicationContext()
                .getSharedPreferences(Configuration.BAKING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Configuration.BAKINGNAME_PREFERENCE_KEY, baking.getName());
        editor.putString(Configuration.INGREDIENTS_PREFERENCE_KEY, getIngredients(baking.getIngredients()));
        editor.commit();

        // Intent Service associated with the Widget
        BakingIntentService.startActionUpdateBakingWidgets(getContext());

        return rootView;
    }

    private String getIngredients(ArrayList<Ingredient> ingredients)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n");

        for(Ingredient ingredient : ingredients)
        {
            stringBuilder.append(" ");
            stringBuilder.append(" ");
            stringBuilder.append(Integer.toString( ingredient.getQuantity()));
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getMeasure());
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getIngredient());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}