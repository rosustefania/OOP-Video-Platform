package commands;

import common.Constants;
import entertainment.SerialSeason;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public final class Rating {
  private final List<UserInputData> users;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  private final String user;

  private final String title;

  private final int id;

  private final Double grade;

  private final int seasonNumber;

  public Rating(
      final List<UserInputData> users,
      final String user,
      final String title,
      final int id,
      final Double grade,
      final int seasonNumber,
      final List<MovieInputData> movies,
      final List<SerialInputData> serials) {
    this.users = users;
    this.user = user;
    this.title = title;
    this.id = id;
    this.grade = grade;
    this.seasonNumber = seasonNumber;
    this.movies = movies;
    this.serials = serials;
  }

  /** method that give rating to a show; */
  public JSONObject giveGrade() {
    JSONObject object = new JSONObject();

    // search for user;
    for (UserInputData u : users) {

      if (u.getUsername().equalsIgnoreCase(user)) {

        // get user's lists;
        Map<String, Integer> history = u.getHistory();
        Map<SerialSeason, Double> serialsRatingList = u.getSerialsRatings();
        Map<String, Double> moviesRatingList = u.getMoviesRatings();

        // verify if the show has been seen;
        for (String name : history.keySet()) {

          if (name.equalsIgnoreCase(title)) {

            // the case where the show is a movie;
            if (seasonNumber == 0) {

              for (String movie : moviesRatingList.keySet()) {

                // verify if the movie has been already rated by
                // this user;
                if (movie.equalsIgnoreCase(title)) {

                  object.put(Constants.ID_STRING, this.id);
                  object.put(
                      Constants.MESSAGE, "error -> " + this.title + " has been already rated");
                  return object;
                }
              }

              // rate it then;
              moviesRatingList.put(this.title, this.grade);

              // add the rating in movie's ratings list;
              for (MovieInputData movie : movies) {

                if (title.equalsIgnoreCase(movie.getTitle())) {

                  movie.getRatings().add(this.grade);
                }
              }

              object.put(Constants.ID_STRING, this.id);
              object.put(
                  Constants.MESSAGE,
                  "success -> "
                      + this.title
                      + " was rated with "
                      + this.grade
                      + " by "
                      + this.user);
              return object;
            }

            // the case where the show is a serial;
            for (SerialSeason serial : serialsRatingList.keySet()) {

              String seriename = serial.getTitle();
              int serieseason = serial.getCurrentSeason();

              // verify if the serial's season has been already rated;
              if (seriename.equalsIgnoreCase(this.title)) {

                if (serieseason == this.seasonNumber) {

                  object.put(Constants.ID_STRING, this.id);
                  object.put(
                      Constants.MESSAGE,
                      "error -> " + this.title + " has been already rated");
                  return object;
                }
              }
            }

            // if not, then rate it;
            SerialSeason serial = new SerialSeason(this.title, this.seasonNumber);
            serialsRatingList.put(serial, this.grade);

            // add the rating in serial's ratings list;
            for (SerialInputData s : serials) {

              if (title.equalsIgnoreCase(s.getTitle())) {

                s.getSeasons().get(seasonNumber - 1).getRatings().add(this.grade);
              }
            }

            object.put(Constants.ID_STRING, this.id);
            object.put(
                Constants.MESSAGE,
                "success -> " + this.title + " was rated with " + this.grade + " by " + this.user);
            return object;
          }
        }

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "error -> " + this.title + " is not seen");
        return object;
      }
    }
    return object;
  }
}
