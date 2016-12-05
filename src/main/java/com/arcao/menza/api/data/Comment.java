package com.arcao.menza.api.data;

import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import java.util.Date;

@AutoValue
@JsonDeserialize(builder = Comment.Builder.class)
public abstract class Comment implements Parcelable {
  @JsonProperty public abstract User user();
  @JsonProperty public abstract Date date();
  @JsonProperty public abstract String text();

  public static Builder builder() {
    return new AutoValue_Comment.Builder();
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return Comment.builder();
    }

    public abstract Builder user(User user);
    public abstract Builder date(Date date);
    public abstract Builder text(String text);
    public abstract Comment build();
  }
}
