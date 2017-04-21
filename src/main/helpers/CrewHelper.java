package main.helpers;

import main.constants.CrewJobs;
import main.model.Crew;

import java.util.ArrayList;
import java.util.List;

public class CrewHelper {

    public static List<Crew> filterDirectors(List<Crew> crew) {
        List<Crew> directors = new ArrayList<>();
        for(Crew crewMember : crew) {
            if (crewMember.getJob().equals(CrewJobs.DIRECTOR)) {
                directors.add(crewMember);
            }
        }
        return directors;
    }

    public static List<Crew> filterWriters(List<Crew> crew) {
        List<Crew> writers = new ArrayList<>();
        for(Crew crewMember: crew) {
            if(crewMember.getJob().equals(CrewJobs.WRITER)) {
                writers.add(crewMember);
            }
        }
        return writers;
    }

}
