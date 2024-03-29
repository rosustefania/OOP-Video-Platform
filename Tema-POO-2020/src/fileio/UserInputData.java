package fileio;

import entertainment.SerialSeason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 *
 * <p>DO NOT MODIFY
 */
public final class UserInputData {
  /** User's username */
  private final String username;
  /** Subscription Type */
  private final String subscriptionType;
  /** The history of the movies seen */
  private final Map<String, Integer> history;
  /** Movies added to favorites */
  private final ArrayList<String> favoriteMovies;

  private final Map<SerialSeason, Double> serialsRatings = new HashMap<>();

  private final Map<String, Double> moviesRatings = new HashMap<>();

  private int givenRatings;

  public UserInputData(
      final String username,
      final String subscriptionType,
      final Map<String, Integer> history,
      final ArrayList<String> favoriteMovies) {
    this.username = username;
    this.subscriptionType = subscriptionType;
    this.favoriteMovies = favoriteMovies;
    this.history = history;
    this.givenRatings = 0;
  }

  public String getUsername() {
    return username;
  }

  public Map<String, Integer> getHistory() {
    return history;
  }

  public ArrayList<String> getFavoriteMovies() {
    return favoriteMovies;
  }

  public Map<SerialSeason, Double> getSerialsRatings() {
    return serialsRatings;
  }

  public Map<String, Double> getMoviesRatings() {
    return moviesRatings;
  }

  public int getGivenRatings() {
    return givenRatings;
  }

  public void setGivenRatings(int givenRatings) {
    this.givenRatings = givenRatings;
  }

  public String getSubscriptionType() {
    return subscriptionType;
  }

  @Override
  public String toString() {
    return "UserInputData{"
        + "username='"
        + username
        + '\''
        + ", subscriptionType='"
        + subscriptionType
        + '\''
        + ", history="
        + history
        + ", favoriteMovies="
        + favoriteMovies
        + '}';
  }
}
