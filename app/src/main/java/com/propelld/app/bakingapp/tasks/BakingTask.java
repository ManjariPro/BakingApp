package com.propelld.app.bakingapp.tasks;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import com.propelld.app.bakingapp.idlingResource.BakingIdlingResource;
import com.propelld.app.bakingapp.models.Baking;
import com.propelld.app.bakingapp.models.Ingredient;
import com.propelld.app.bakingapp.models.Step;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manjari on 27/5/18.
 */

public class BakingTask extends AsyncTask <String, Void, String>
{
    private ArrayList<Baking> bakings;
    private OnTaskCompleted onCompleted;
    private final BakingIdlingResource idlingResource;

    public BakingTask(ArrayList<Baking> bakings,
                      OnTaskCompleted onCompleted,
                      @Nullable final BakingIdlingResource idlingResource)
    {
        this.bakings = bakings;
        this.onCompleted = onCompleted;
        this.idlingResource = idlingResource;
    }

    @Override
    protected String doInBackground(String... urls)
    {
        if (idlingResource != null)
        {
            idlingResource.setIdleState(false);
        }

        StringBuilder stringBuilder = new StringBuilder();

        try
        {
            URL url = new URL(urls[0]);

            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        try
        {
            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                Baking baking =  new Baking();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int servings = jsonObject.getInt("servings");
                String image = jsonObject.getString("image");

                baking.setId(id);
                baking.setName(name);
                baking.setServings(servings);
                baking.setImage(image);

                // populating ingredients
                JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");

                ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

                for (int j = 0; j <ingredientsArray.length(); j++)
                {
                    JSONObject ingredientObj = ingredientsArray.getJSONObject(j);

                    int quantity = ingredientObj.getInt("quantity");
                    String measure = ingredientObj.getString("measure");
                    String ingredientString = ingredientObj.getString("ingredient");

                    Ingredient ingredient = new Ingredient();

                    ingredient.setQuantity(quantity);
                    ingredient.setMeasure(measure);
                    ingredient.setIngredient(ingredientString);

                    ingredients.add(ingredient);
                }

                baking.setIngredients(ingredients);
                // end populating ingredients

                // populating Steps
                JSONArray stepsArray = jsonObject.getJSONArray("steps");

                ArrayList<Step> steps = new ArrayList<Step>();

                for ( int k = 0; k < stepsArray.length(); k++)
                {
                    JSONObject stepsObj = stepsArray.getJSONObject(k);

                    int stepId = stepsObj.getInt("id");
                    String shortDescription = stepsObj.getString("shortDescription");
                    String description = stepsObj.getString("description");
                    String videoUrl = stepsObj.getString("videoURL");
                    String thumbnailURL = stepsObj.getString("thumbnailURL");

                    Step step = new Step();
                    step.setId(stepId);
                    step.setShortDescription(shortDescription);
                    step.setDescription(description);
                    step.setVideoURL(videoUrl);
                    step.setThumbnailURL(thumbnailURL);

                    steps.add(step);
                }

                baking.setSteps(steps);
                // End populating Steps

                bakings.add(baking);
            }

            onCompleted.onTaskCompleted();

            if (idlingResource != null)
            {
                idlingResource.setIdleState(true);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}