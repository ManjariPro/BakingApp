package com.propelld.app.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manjari on 27/5/18.
 */

public class Baking implements Parcelable, Serializable
{
    private int id;
    private String name;
    private int servings;
    private String image;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getServings()
    {
        return servings;
    }

    public void setServings(int servings)
    {
        this.servings = servings;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public ArrayList<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps()
    {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps)
    {
        this.steps = steps;
    }

    public Baking()
    {
    }

    public Baking(Parcel parcel)
    {
       id = parcel.readInt();
       name = parcel.readString();
       servings = parcel.readInt();
       image = parcel.readString();
       ingredients = parcel.readArrayList(null);
       steps = parcel.readArrayList(null);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }

    public static Creator<Baking> CREATOR = new Creator<Baking>()
    {
        @Override
        public Baking createFromParcel(Parcel source)
        {
            return new Baking(source);
        }

        @Override
        public Baking[] newArray(int size)
        {
            return new Baking[size];
        }
    };
}