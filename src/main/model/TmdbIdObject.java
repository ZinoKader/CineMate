package main.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TmdbIdObject implements Serializable {

    @SerializedName("id")
    private int id;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



    @Override
    public int hashCode() {
        return id;
    }
}