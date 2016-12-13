package com.crossover_assignment.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kate on 12/10/16.
 */

public class Place implements Parcelable {
    private String id;
    private String name;
    private Location location;

    public Place(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    /** Getters **/
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    /** Setters **/
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.location, flags);
    }

    protected Place(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
