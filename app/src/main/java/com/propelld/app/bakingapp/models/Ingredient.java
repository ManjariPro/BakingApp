package com.propelld.app.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manjari on 27/5/18.
 */

public class Ingredient implements Parcelable
{
    private int quantity;
    private String measure;
    private String ingredient;

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getMeasure()
    {
        return measure;
    }

    public void setMeasure(String measure)
    {
        this.measure = measure;
    }

    public String getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(String ingredient)
    {
        this.ingredient = ingredient;
    }

    public Ingredient()
    {
    }

    public Ingredient(Parcel parcel)
    {
        quantity = parcel.readInt();
        measure = parcel.readString();
        ingredient = parcel.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static Creator<Ingredient> CREATOR = new Creator<Ingredient>()
    {
        @Override
        public Ingredient createFromParcel(Parcel source)
        {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size)
        {
            return new Ingredient[size];
        }
    };
}