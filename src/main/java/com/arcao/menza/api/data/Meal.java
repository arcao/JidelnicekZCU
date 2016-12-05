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
@JsonDeserialize(builder = Meal.Builder.class)
public abstract class Meal implements Parcelable {
  @JsonProperty public abstract int id();
  @JsonProperty public abstract String name();
  @JsonProperty public abstract float priceStudent();
  @JsonProperty public abstract float priceStaff();
  @JsonProperty public abstract float priceExternal();
  @JsonProperty public abstract String hash();
  @JsonProperty public abstract float quality();
  @JsonProperty public abstract int commentCount();
  @JsonProperty public abstract int imageCount();
  @JsonProperty public abstract List<Image> images();
  @JsonProperty public abstract List<Comment> comments();
  @JsonProperty public abstract List<Integer> allergens();
  @JsonProperty public abstract boolean premium();

  public static Builder builder() {
    return new AutoValue_Meal.Builder().quality(-1)
        .commentCount(0)
        .imageCount(0)
        .images(Collections.emptyList())
        .comments(Collections.emptyList())
        .allergens(Collections.emptyList());
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class Builder {
    @JsonCreator private static Builder create() {
      return Meal.builder();
    }

    public abstract Builder id(int id);
    public abstract Builder name(String name);
    public abstract Builder priceStudent(float priceStudent);
    public abstract Builder priceStaff(float priceStaff);
    public abstract Builder priceExternal(float priceExternal);
    public abstract Builder hash(String hash);
    public abstract Builder quality(float quality);
    public abstract Builder commentCount(int commentCount);
    public abstract Builder imageCount(int imageCount);
    public abstract Builder images(List<Image> images);
    public abstract Builder comments(List<Comment> comments);
    public abstract Builder allergens(List<Integer> allergens);
    public abstract Builder premium(boolean premium);
    public abstract Meal build();
  }
}
