package main.helpers;

import main.constants.CrewJobs;
import main.model.Crew;

import java.util.ArrayList;
import java.util.List;

public class CrewHelper {

    public static Crew filterDirector(List<Crew> crew) {
        for(Crew crewMember : crew) {
            if(crewMember.getJob().equals(CrewJobs.DIRECTOR)) {
                return crewMember;
            }
        }
        //TODO: LOL BETTER SOLUTION PLS
        return new Crew();
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
