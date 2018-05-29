package com.propelld.app.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/**
 * Created by manjari on 27/5/18.
 */

public class Step implements Parcelable, Serializable
{
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getVideoURL()
    {
        return videoURL;
    }

    public void setVideoURL(String videoURL)
    {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL()
    {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL;
    }

    public Step()
    {
    }

    public Step(Parcel parcel)
    {
        id = parcel.readInt();
        shortDescription = parcel.readString();
        description = parcel.readString();
        videoURL = parcel.readString();
        thumbnailURL = parcel.readString();
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
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static Creator<Step> CREATOR = new Creator<Step>()
    {
        @Override
        public Step createFromParcel(Parcel source)
        {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size)
        {
            return new Step[size];
        }
    };
}