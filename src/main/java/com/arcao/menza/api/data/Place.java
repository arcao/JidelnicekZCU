package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
	public String name;
	public String address;
	public String description;
	public String[] images;

	public Place() {}

	protected Place(Parcel in) {
		name = in.readString();
		address = in.readString();
		description = in.readString();
		images = in.createStringArray();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(description);
		dest.writeArray(images);
	}

	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		@Override
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		@Override
		public Place[] newArray(int size) {
			return new Place[size];
		}
	};
}
