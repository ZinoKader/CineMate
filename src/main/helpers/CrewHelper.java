package main.helpers;

import main.constants.CrewJobs;
import main.model.Crew;

import java.util.List;
import java.util.stream.Collectors;

public class CrewHelper {

    public static List<Crew> filterDirectors(List<Crew> crew) {
        /* Could be done like this, but that's lame
        List<Crew> directors = new ArrayList<>();
        for(Crew crewMember : crew) {
            if(crewMember.getJob().equals(CrewJobs.DIRECTOR)) {
                directors.add(crewMember);
            }
        }
        return directors;
        */

        return crew.stream().filter(crewMember -> crewMember.getJob().equals(CrewJobs.DIRECTOR)).collect(Collectors.toList());
    }

}
