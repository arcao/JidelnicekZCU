package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Meal implements Parcelable {
	public int id;
	public String name;
	public float priceStudent;
	public float priceStaff;
	public float priceExternal;
	public String hash;
	public float quality;
	public int commentCount;
	public int imageCount;
	public Image[] images;
	public Comment[] comments;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeFloat(this.priceStudent);
		dest.writeFloat(this.priceStaff);
		dest.writeFloat(this.priceExternal);
		dest.writeString(this.hash);
		dest.writeFloat(this.quality);
		dest.writeInt(this.commentCount);
		dest.writeInt(this.imageCount);
		dest.writeTypedArray(this.images, flags);
		dest.writeTypedArray(this.comments, flags);
	}

	public Meal() {
	}

	private Meal(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.priceStudent = in.readFloat();
		this.priceStaff = in.readFloat();
		this.priceExternal = in.readFloat();
		this.hash = in.readString();
		this.quality = in.readFloat();
		this.commentCount = in.readInt();
		this.imageCount = in.readInt();
		this.images = in.createTypedArray(Image.CREATOR);
		this.comments = in.createTypedArray(Comment.CREATOR);
	}

	public static Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
		public Meal createFromParcel(Parcel source) {
			return new Meal(source);
		}

		public Meal[] newArray(int size) {
			return new Meal[size];
		}
	};
}
