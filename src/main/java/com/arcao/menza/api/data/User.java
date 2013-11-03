package com.arcao.menza.api.data;

/**
 * Created by msloup on 17.9.13.
 */
public class User {
    public int id;
    public UserType type;
    public String login;
    public String firstname;
    public String lastname;
	public String publicProfileUrl;
    public String imageUrl;

    public static enum UserType {
        GOOGLE,
        FACEBOOK;
    }
}
