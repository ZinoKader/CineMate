package main.model;

import com.google.gson.annotations.SerializedName;

public class Crew extends Person {

    @SerializedName("department")
    private String department;

    @SerializedName("job")
    private String job;

    public Crew() {
    }

    public String getJob() {
        return job;
    }

    public String getDepartment() {
        return department;
    }

}
