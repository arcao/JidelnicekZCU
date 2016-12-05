package com.arcao.menza.api.data;

import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = Image.Builder.class)
public abstract class Image implements Parcelable {
  @JsonProperty public abstract String imageUrl();
  @JsonProperty public abstract User user();
  @JsonProperty public abstract String description();

  public static Builder builder() {
    return new AutoValue_Image.Builder();
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return Image.builder();
    }

    public abstract Builder imageUrl(String imageUrl);
    public abstract Builder user(User user);
    public abstract Builder description(String description);
    public abstract Image build();
  }
}
