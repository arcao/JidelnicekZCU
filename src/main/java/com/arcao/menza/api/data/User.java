package com.arcao.menza.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	public int id;
	public UserType type;
	public String login;
	public String firstname;
	public String lastname;
	public String publicProfileUrl;
	public String imageUrl;

	public static enum UserType {
		GOOGLE,
		FACEBOOK
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.type == null ? -1 : this.type.ordinal());
		dest.writeString(this.login);
		dest.writeString(this.firstname);
		dest.writeString(this.lastname);
		dest.writeString(this.publicProfileUrl);
		dest.writeString(this.imageUrl);
	}

	public User() {
	}

	private User(Parcel in) {
		this.id = in.readInt();
		int tmpType = in.readInt();
		this.type = tmpType == -1 ? null : UserType.values()[tmpType];
		this.login = in.readString();
		this.firstname = in.readString();
		this.lastname = in.readString();
		this.publicProfileUrl = in.readString();
		this.imageUrl = in.readString();
	}

	public static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
