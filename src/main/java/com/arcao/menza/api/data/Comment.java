package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by msloup on 3.11.13.
 */
public class Comment implements Parcelable {
	User user;
	Date date;
	String text;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.user, flags);
		dest.writeLong(date != null ? date.getTime() : -1);
		dest.writeString(this.text);
	}

	public Comment() {
	}

	private Comment(Parcel in) {
		this.user = in.readParcelable(User.class.getClassLoader());
		long tmpDate = in.readLong();
		this.date = tmpDate == -1 ? null : new Date(tmpDate);
		this.text = in.readString();
	}

	public static Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}

		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
}
