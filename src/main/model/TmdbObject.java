package main.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public interface TmdbObject {


    /**
     * For automatic mapping to the right class when using Multi Search
     */
    @JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property="media_type")
    @JsonSubTypes({ @Type(value = Movie.class, name = "movie"), @Type(value = Person.class, name = "person"), @Type(value = Series.class, name = "tv")})


    /**
     * Nice way to not use instanceof or getclass, instead letting the object tell us about itself
     */
    public MediaType getMediaType();


}
