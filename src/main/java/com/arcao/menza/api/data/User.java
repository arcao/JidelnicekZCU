package com.arcao.menza.api.data;

import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = User.Builder.class)
public abstract class User implements Parcelable {
  @JsonProperty public abstract int id();
  @JsonProperty public abstract UserType type();
  @JsonProperty public abstract String login();
  @JsonProperty public abstract String firstName();
  @JsonProperty public abstract String lastName();
  @JsonProperty public abstract String publicProfileUrl();
  @JsonProperty public abstract String imageUrl();

  public static Builder builder() {
    return new AutoValue_User.Builder();
  }

  public enum UserType {
    GOOGLE,
    FACEBOOK
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return User.builder();
    }

    public abstract Builder id(int id);
    public abstract Builder type(UserType type);
    public abstract Builder login(String login);
    public abstract Builder firstName(String firstName);
    public abstract Builder lastName(String lastName);
    public abstract Builder publicProfileUrl(String publicProfileUrl);
    public abstract Builder imageUrl(String imageUrl);
    public abstract User build();
  }
}
