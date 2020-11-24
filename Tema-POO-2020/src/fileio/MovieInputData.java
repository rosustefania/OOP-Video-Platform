package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 *
 * <p>DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
  /** Duration in minutes of a season */
  private final int duration;

  private final List<Double> ratings = new ArrayList<>();

  /** Movie's average rating */
  private Double ratingsMean;

  /** Movie's number of appearences in favourites' lists */
  private int favouriteAppeareances;

  /** Movie's number of views */
  private int views;

  public MovieInputData(final String title, final ArrayList<String> cast,
                        final ArrayList<String> genres, final int year, final int duration) {
    super(title, year, cast, genres);
    this.duration = duration;
    this.ratingsMean = 0.0;
    this.favouriteAppeareances = 0;
    this.views = 0;
  }

  public  int getDuration() {
    return duration;
  }

  public List<Double> getRatings() {
    return ratings;
  }

  public Double getRatingsMean() {
    return ratingsMean;
  }

  public void setRatingsMean(Double ratingsMean) {
    this.ratingsMean = ratingsMean;
  }

  public int getFavouriteAppeareances() {
    return favouriteAppeareances;
  }

  public void setFavouriteAppeareances(int favouriteAppeareances) {
    this.favouriteAppeareances = favouriteAppeareances;
  }

  public int getViews() {
    return views;
  }

  public void setViews(int views) {
    this.views = views;
  }

  @Override
  public String toString() {
    return "MovieInputData{"
        + "title= "
        + super.getTitle()
        + "year= "
        + super.getYear()
        + "duration= "
        + duration
        + "cast {"
        + super.getCast()
        + " }\n"
        + "genres {"
        + super.getGenres()
        + " }\n ";
  }

  /** method that calculates the average rating of the movie */
  public void mean() {
    if (!ratings.isEmpty()) {
      Double sum = 0.0;

      for (Double rating : ratings) {

        sum += rating;
      }

      this.ratingsMean = sum / ratings.size();
    }
  }
}
