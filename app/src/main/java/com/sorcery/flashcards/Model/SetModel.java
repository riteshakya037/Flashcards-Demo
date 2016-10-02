package com.sorcery.flashcards.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ritesh Shakya on 8/28/2016.
 */

public class SetModel implements Parcelable {
    public String displayName;
    public String setLocation;

    @Override
    public String toString() {
        return "SetModel{" +
                "displayName='" + displayName + '\'' +
                ", setLocation='" + setLocation + '\'' +
                '}';
    }

    public SetModel() {
    }

    public SetModel(String displayName, String setLocation) {
        this.displayName = displayName;
        this.setLocation = setLocation;
    }

    private SetModel(Parcel in) {
        displayName = in.readString();
        setLocation = in.readString();
    }

    public static final Creator<SetModel> CREATOR = new Creator<SetModel>() {
        @Override
        public SetModel createFromParcel(Parcel in) {
            return new SetModel(in);
        }

        @Override
        public SetModel[] newArray(int size) {
            return new SetModel[size];
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public String getSetLocation() {
        return setLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(setLocation);
    }
}
