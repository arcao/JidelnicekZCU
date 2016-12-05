package com.arcao.menza.api.data;

import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
@JsonDeserialize(builder = Section.Builder.class)
public abstract class Section implements Parcelable {
  @JsonProperty public abstract String name();
  @JsonProperty public abstract List<Meal> meals();

  public static Builder builder() {
    return new AutoValue_Section.Builder();
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return Section.builder();
    }

    public abstract Builder name(String name);
    public abstract Builder meals(List<Meal> meals);
    public abstract Section build();
  }
}
