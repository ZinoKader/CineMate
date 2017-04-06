package main.model;

import com.google.gson.annotations.SerializedName;

public class Cast extends Person {

    @SerializedName("character")
    private String character;

    @SerializedName("order")
    private int order;

    public Cast() {
    }

    public String getCharacter() {
        return character;
    }

    public int getOrder() {
        return order;
    }

}