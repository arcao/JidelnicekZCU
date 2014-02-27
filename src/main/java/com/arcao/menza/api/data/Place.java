package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by msloup on 10.11.13.
 */
public class Place implements Parcelable {
	public String name;
	public String description;
	public Image[] images;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeTypedArray(this.images, flags);
	}

	public Place() {
	}

	private Place(Parcel in) {
		this.name = in.readString();
		this.description = in.readString();
		this.images = in.createTypedArray(Image.CREATOR);
	}

	public static Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		public Place createFromParcel(Parcel source) {
			return new Place(source);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};
}
