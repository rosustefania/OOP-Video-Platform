package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 *
 * <p>DO NOT MODIFY
 */
public final class ActorInputData {
  /** actor name */
  private String name;
  /** description of the actor's career */
  private String careerDescription;
  /** videos starring actor */
  private ArrayList<String> filmography;
  /** awards won by the actor */
  private Map<ActorsAwards, Integer> awards;
  /** number of awards won by the actor */
  private Double rating;
  /** number of awards won by the actor */
  private int awardsWon;

  public ActorInputData(final String name, final String careerDescription,
                        final ArrayList<String> filmography,
                        final Map<ActorsAwards, Integer> awards) {
    this.name = name;
    this.careerDescription = careerDescription;
    this.filmography = filmography;
    this.awards = awards;
    this.awardsWon = 0;
  }

  public ActorInputData() { }

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

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public int getAwardsWon() {
    return awardsWon;
  }

  public void setAwardsWon(int awardsWon) {
    this.awardsWon = awardsWon;
  }

  @Override
  public String toString() {
    return "ActorInputData{"
        + "name='"
        + name
        + '\''
        + ", careerDescription='"
        + careerDescription
        + '\''
        + ", filmography="
        + filmography
        + '}';
  }
}
