package com.johnson.pablo.popularmovies.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.johnson.pablo.popularmovies.models.data.VideoColumns;

/**
 * Created by Pablo on 11/01/16.
 */
public class Video implements Parcelable {

    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public String getVideoPath() {
        if (site != null && key != null) {
            String url = site;
            if (site.equalsIgnoreCase("youtube")) {
                url = String.format("https://www.youtube.com/watch?v=%s", key);
            }
            return url;
        }
        return null;
    }

    public static ContentValues getContentValue(Video video, long movieId) {
        ContentValues videoVContentValues = new ContentValues();
        videoVContentValues.put(VideoColumns.VIDEO_ID, video.getId());
        videoVContentValues.put(VideoColumns.MOVIE_ID, movieId);
        videoVContentValues.put(VideoColumns.NAME, video.getName());
        videoVContentValues.put(VideoColumns.SITE, video.getSite());
        videoVContentValues.put(VideoColumns.KEY, video.getKey());

        return videoVContentValues;
    }
}
