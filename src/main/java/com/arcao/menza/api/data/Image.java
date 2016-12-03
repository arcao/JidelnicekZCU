package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    String imageUrl;
    User user;
    String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.description);
    }

    public Image() {
    }

    private Image(Parcel in) {
        this.imageUrl = in.readString();
        this.user = in.readParcelable(((Object) user).getClass().getClassLoader());
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
