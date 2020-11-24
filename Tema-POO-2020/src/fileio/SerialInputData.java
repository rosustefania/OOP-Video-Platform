package fileio;

import entertainment.Season;

import java.util.ArrayList;

/**
 * Information about a tv show, retrieved from parsing the input test files
 *
 * <p>DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
  /** Number of seasons */
  private final int numberOfSeasons;

  /** Season list */
  private final ArrayList<Season> seasons;

  /** Serial's average rating */
  private Double ratingsMean;

  /** Number of appearences in favourites' lists */
  private int favouriteAppearences;

  /** Total duration */
  private int duration;

  /** Number of views */
  private int views;

  public SerialInputData(final String title, final ArrayList<String> cast,
                         final ArrayList<String> genres, final int numberOfSeasons,
                         final ArrayList<Season> seasons, final int year) {
    super(title, year, cast, genres);
    this.numberOfSeasons = numberOfSeasons;
    this.seasons = seasons;
    this.ratingsMean = 0.0;
    this.favouriteAppearences = 0;
    this.duration = 0;
  }

  public int getNumberSeason() {
    return numberOfSeasons;
  }

  public ArrayList<Season> getSeasons() {
    return seasons;
  }

  public Double getRatingsMean() {
    return ratingsMean;
  }

  public int getFavouriteAppearences() {
    return favouriteAppearences;
  }

  public void setFavouriteAppearences(int favouriteAppearences) {
    this.favouriteAppearences = favouriteAppearences;
  }

  public int getDuration() {
    return duration;
  }

  public int getViews() {
    return views;
  }

  public void setViews(int views) {
    this.views = views;
  }

  /** calculate total duration */
  public void calculateDuration() {
    int time = 0;

    for (Season season : seasons) {

      time += season.getDuration();
    }

    this.duration = time;
  }

  /** calculate the average rating of the serial */
  public void mean() {
    double sum = 0.0;

    for (Season season : seasons) {

      season.mean();
      Double seasonMean = season.getRatingsMean();

      if (seasonMean != 0) {

        sum += seasonMean;
      }
    }

    if (sum != 0) {

      ratingsMean = sum / numberOfSeasons;
    }
  }

  @Override
  public String toString() {
    return "SerialInputData{"
        + " title= "
        + super.getTitle()
        + " "
        + " year= "
        + super.getYear()
        + " cast {"
        + super.getCast()
        + " }\n"
        + " genres {"
        + super.getGenres()
        + " }\n "
        + " numberSeason= "
        + numberOfSeasons
        + ", seasons="
        + seasons
        + "\n\n"
        + '}';
  }
}
