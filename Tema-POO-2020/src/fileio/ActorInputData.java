package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class ActorInputData {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;

    /**
     * number of awards won by the actor
     */
    private int awards_won;

    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        this.awards_won = 0;
    }

    public ActorInputData(){}

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public int getAwards_won() {
        return awards_won;
    }

    public void setAwards_won(int awards_won) {
        this.awards_won = awards_won;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }



}
