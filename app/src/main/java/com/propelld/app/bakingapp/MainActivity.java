package com.propelld.app.bakingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import com.propelld.app.bakingapp.adapters.BakingAdapter;
import com.propelld.app.bakingapp.adapters.OnBakingRecipeClick;
import com.propelld.app.bakingapp.adapters.OnStepClick;
import com.propelld.app.bakingapp.fragments.DescriptionFragment;
import com.propelld.app.bakingapp.fragments.DetailViewFragment;
import com.propelld.app.bakingapp.idlingResource.BakingIdlingResource;
import com.propelld.app.bakingapp.models.Baking;
import com.propelld.app.bakingapp.models.Step;
import com.propelld.app.bakingapp.tasks.BakingTask;
import com.propelld.app.bakingapp.tasks.OnTaskCompleted;
import com.propelld.app.bakingapp.tasks.TaskListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, OnBakingRecipeClick, OnStepClick, TaskListener
{
    private ArrayList<Baking> bakings;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewTab;
    private BakingAdapter bakingAdapter;
    private boolean isRecyclerViewVisible = true;

    // The Idling Resource which will be null in production.
    @Nullable private BakingIdlingResource mIdlingResource;
    private final String RECIPE_TAG = "RECIPE";
    private final String STEPS_TAG  = "STEPS";
    private final String BAKING_SAVED_INSTANCE = "BAKING_SAVED_INSTANCE";
    private final String RECYCLERVIEW_VISIBLE = "RECYCLERVIEW_VISIBLE";

    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.MainActivityTitle);

        bakings = new ArrayList<Baking>();
        recyclerView = (RecyclerView) findViewById(R.id.receipe);
        recyclerViewTab = (RecyclerView) findViewById(R.id.receipeTab);
        bakingAdapter = new BakingAdapter(bakings, MainActivity.this, this);

        if (savedInstanceState != null
                && (savedInstanceState.getSerializable("BAKING_SAVED_INSTANCE") != null))
        {
            ArrayList<Baking> bakingsStored =
                    (ArrayList<Baking>) savedInstanceState.getSerializable("BAKING_SAVED_INSTANCE");

            bakings.clear();
            for (Baking baking : bakingsStored)
            {
                bakings.add(baking);
            }
            isRecyclerViewVisible = savedInstanceState.getBoolean("RECYCLERVIEW_VISIBLE");
        }
        else
        {
            BakingTask task = new BakingTask(bakings, this,this, mIdlingResource);
            task.execute(Configuration.url);
        }

        if (recyclerView != null)
        {
            recyclerView
                    .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(bakingAdapter);

            if (!isRecyclerViewVisible)
            {
                recyclerView.setVisibility(View.GONE);
            }
        }
        else
        {
           recyclerViewTab
                   .setLayoutManager(new GridLayoutManager(this, 2));
           recyclerViewTab.setAdapter(bakingAdapter);

           if (!isRecyclerViewVisible)
           {
               recyclerViewTab.setVisibility(View.GONE);
           }
        }

        bakingAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putSerializable("BAKING_SAVED_INSTANCE", bakings);
        state.putBoolean("RECYCLERVIEW_VISIBLE", isRecyclerViewVisible);
    }

    @Override
    public void onTaskCompleted()
    {
        bakingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBakingRecipeClick(Baking baking)
    {
        if (recyclerView != null)
        {
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            recyclerViewTab.setVisibility(View.GONE);
        }
        isRecyclerViewVisible = false;
        // Now launch the fragment to show the details...
        DetailViewFragment fragment = new DetailViewFragment();
        fragment.setBaking(baking);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainActivity, fragment,RECIPE_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStepClick(Step step)
    {
        if (recyclerView != null)
        {
            // Now launch the fragment to show the details...
            DescriptionFragment fragment = new DescriptionFragment();
            fragment.setStep(step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainActivity, fragment, STEPS_TAG)
                    .addToBackStack(RECIPE_TAG)
                    .commit();
        }
        else
        {
            DescriptionFragment fragment = new DescriptionFragment();
            fragment.setStep(step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.tabvideo, fragment, STEPS_TAG)
                    .addToBackStack(RECIPE_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        if(!getSupportFragmentManager().popBackStackImmediate())
        {
            finish();
        }
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            if (recyclerView != null)
            {
                recyclerView.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else
            {
                recyclerViewTab.setVisibility(View.VISIBLE);
            }
            isRecyclerViewVisible = true;
            setTitle(R.string.MainActivityTitle);
        }
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (mIdlingResource == null)
        {
            mIdlingResource = new BakingIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case  android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskStarted()
    {
        isTaskRunning = true;
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait a moment!");
    }

    @Override
    public void onTaskFinished()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
}