package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Crew in a MotionPicture, is a Person with a job and job department attached to the MotionPicture
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
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
