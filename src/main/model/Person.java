package main.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gson.annotations.SerializedName;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)

public class Person implements TmdbObject {

    @SerializedName("name")
    private String name;

    public Person() {

    }

    @Override public MediaType getMediaType() {
	return MediaType.PERSON;
    }

    public String getName() {
	return name;
    }
}
