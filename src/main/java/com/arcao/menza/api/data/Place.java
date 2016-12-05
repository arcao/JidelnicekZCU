package com.arcao.menza.api.data;

import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import java.util.Collections;
import java.util.List;

@AutoValue
@JsonDeserialize(builder = Place.Builder.class)
public abstract class Place implements Parcelable {
  @JsonProperty public abstract String name();
  @JsonProperty public abstract String address();
  @JsonProperty public abstract String description();
  @JsonProperty public abstract List<String> images();

  public static Builder builder() {
    return new AutoValue_Place.Builder()
        .images(Collections.emptyList());
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return Place.builder();
    }

    public abstract Builder name(String name);
    public abstract Builder address(String address);
    public abstract Builder description(String description);
    public abstract Builder images(List<String> images);
    public abstract Place build();
  }
}
