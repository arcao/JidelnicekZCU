package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Section implements Parcelable {
    public String name;
    public Meal[] meals;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedArray(this.meals, flags);
    }

    public Section() {
    }

    private Section(Parcel in) {
        this.name = in.readString();
        this.meals = in.createTypedArray(Meal.CREATOR);
    }

    public static Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        public Section createFromParcel(Parcel source) {
            return new Section(source);
        }

        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
}
