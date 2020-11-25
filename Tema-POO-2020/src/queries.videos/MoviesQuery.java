package queries.videos;

import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoviesQuery {
  private final List<UserInputData> users;

  private final List<MovieInputData> givenMovies;

  private final int id;

  private final String sortType;

  private final int number;

  private final List<String> years;

  private final List<String> genres;

  public MoviesQuery(final List<UserInputData> users, final List<MovieInputData> givenMovies,
                     final int id, final String sortType, final int number,
                     final List<String> years, final List<String> genres) {
    this.users = users;
    this.givenMovies = givenMovies;
    this.id = id;
    this.sortType = sortType;
    this.number = number;
    this.years = years;
    this.genres = genres;
  }

  /** methods that adds in the query the first n givenMovies based on their rating */
  public final JSONObject getRatingMovies() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<MovieInputData> sortedMovies = new ArrayList<>(givenMovies);

    // sort movies' list ascendent by average rating;
    if (sortType.equalsIgnoreCase("asc")) {
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          // calculate movies' average rating;
          sortedMovies.get(j).mean();
          sortedMovies.get(j + 1).mean();

          if (sortedMovies.get(j).getRatingsMean() > sortedMovies.get(j + 1).getRatingsMean()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same average rating, sort tem ascendent by name;
          if ((sortedMovies.get(j).getRatingsMean().equals(sortedMovies.get(j + 1)
                  .getRatingsMean())) && (sortedMovies.get(j).getTitle()
                  .compareTo(sortedMovies.get(j + 1).getTitle()) > 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort movies' list descendent by average rating;
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          // calculate movies' average rating;
          sortedMovies.get(j).mean();
          sortedMovies.get(j + 1).mean();

          if (sortedMovies.get(j).getRatingsMean() < sortedMovies.get(j + 1).getRatingsMean()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same average rating, sort tem descendent by name;
          if ((sortedMovies.get(j).getRatingsMean().equals(sortedMovies.get(j + 1)
                  .getRatingsMean())) && (sortedMovies.get(j).getTitle()
                  .compareTo(sortedMovies.get(j + 1).getTitle()) < 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    int count = 0;
    for (MovieInputData movie : sortedMovies) {

      if (count < number) {

        movie.mean();

        // verify if the movie's average rating is 0;
        if (movie.getRatingsMean() != 0) {

          boolean flag1 = true;
          boolean flag2 = true;

          // apply year filter;
          if (years.get(0) != null) {

            flag1 = false;
            String yearString = String.valueOf(movie.getYear());
            for (String year : years) {

              if (year.equalsIgnoreCase(yearString)) {

                flag1 = true;
                break;
              }
            }
          }

          // apply genres filter;
          if (genres.get(0) != null) {

            if (!flag1) {

              flag2 = false;
            } else {

              int genresFound = 0;
              for (String genre : genres) {

                for (String movieGenre : movie.getGenres()) {

                  if (genre.equalsIgnoreCase(movieGenre)) {

                    genresFound++;
                  }
                }
              }
              if (genresFound != genres.size()) {

                flag2 = false;
              }
            }
          }

          if (flag1 && flag2) {

            query.add(movie.getTitle());
            count++;
          }
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }

  /** methods that adds in the query the first n givenMovies based on the number of
   * appereances in favorites' lists */
  public final JSONObject getFavouriteMovies() {

    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<MovieInputData> sortedMovies = new ArrayList<>(givenMovies);

    for (MovieInputData movie : sortedMovies) {

      int count = 0;
      String title = movie.getTitle();

      for (UserInputData user : users) {

        List<String> favourites = user.getFavoriteMovies();
        for (String favourite : favourites) {

          if (favourite.equalsIgnoreCase(title)) {

            count++;
            break;
          }
        }
      }

      movie.setFavouriteAppeareances(count);
    }

    // sort movies' list ascendent by appearences' number;
    if (sortType.equalsIgnoreCase("asc")) {
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getFavouriteAppeareances()
                  > sortedMovies.get(j + 1).getFavouriteAppeareances()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same appearences' number, sort them ascendent by name;
          if ((sortedMovies.get(j).getFavouriteAppeareances()
                  == sortedMovies.get(j + 1).getFavouriteAppeareances())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies.get(j + 1)
                  .getTitle()) > 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort movies' list descendent by appearences' number;
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getFavouriteAppeareances()
                  < sortedMovies.get(j + 1).getFavouriteAppeareances()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same appearences' number, sort them descendent by name;
          if ((sortedMovies.get(j).getFavouriteAppeareances()
                  == sortedMovies.get(j + 1).getFavouriteAppeareances())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies.get(j + 1)
                  .getTitle()) < 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    int count = 0;
    for (MovieInputData movie : sortedMovies) {

      if (count < number) {

        // verify if the movie's appearences' number is 0;
        if (movie.getFavouriteAppeareances() != 0) {

          boolean flag1 = true;
          boolean flag2 = true;

          // apply year filter;
          if (years.get(0) != null) {

            flag1 = false;
            String yearString = String.valueOf(movie.getYear());
            for (String year : years) {

              if (year.equalsIgnoreCase(yearString)) {

                flag1 = true;
                break;
              }
            }
          }

          // apply genres filter;
          if (genres.get(0) != null) {

            if (!flag1) {

              flag2 = false;
            } else {

              int genresFound = 0;
              for (String genre : genres) {

                for (String movieGenre : movie.getGenres()) {

                  if (genre.equalsIgnoreCase(movieGenre)) {

                    genresFound++;
                  }
                }
              }
              if (genresFound != genres.size()) {

                flag2 = false;
              }
            }
          }

          if (flag1 && flag2) {

            query.add(movie.getTitle());
            count++;
          }
        }
      }
    }
    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }

  /** methods that adds in the query the first n givenMovies based on their duration */
  public final JSONObject getLongestMovies() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<MovieInputData> sortedMovies = new ArrayList<>(givenMovies);

    // sort movies' list ascendent by duration;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getDuration() > sortedMovies.get(j + 1).getDuration()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same duration, sort them ascendent by name;
          if ((sortedMovies.get(j).getDuration() == sortedMovies.get(j + 1).getDuration())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies
                  .get(j + 1).getTitle()) > 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort movies' list descendent by duration;
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getDuration() < sortedMovies.get(j + 1).getDuration()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same duration, sort them descendent by name;
          if ((sortedMovies.get(j).getDuration() == sortedMovies.get(j + 1).getDuration())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies.get(j + 1)
                  .getTitle()) < 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    int count = 0;
    for (MovieInputData serial : sortedMovies) {

      if (count < number) {

        boolean flag1 = true;
        boolean flag2 = true;

        // apply year filter;
        if (years.get(0) != null) {

          flag1 = false;
          String yearString = String.valueOf(serial.getYear());
          for (String year : years) {

            if (year.equalsIgnoreCase(yearString)) {

              flag1 = true;
              break;
            }
          }
        }

        // apply genres filter;
        if (genres.get(0) != null) {

          if (!flag1) {

            flag2 = false;
          } else {

            int genresFound = 0;
            for (String genre : genres) {

              for (String movieGenre : serial.getGenres()) {

                if (genre.equalsIgnoreCase(movieGenre)) {

                  genresFound++;
                }
              }
            }

            if (genresFound != genres.size()) {

              flag2 = false;
            }
          }
        }

        if (flag1 && flag2) {

          query.add(serial.getTitle());
          count++;
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }

  /** methods that adds in the query the first n givenMovies based on their views */
  public final JSONObject getMostViewedMovies() {

    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<MovieInputData> sortedMovies = new ArrayList<>(givenMovies);

    // calculate views number for every movie;
    for (MovieInputData movie : sortedMovies) {

      int viewsNumber = 0;
      for (UserInputData user : users) {

        Map<String, Integer> history = user.getHistory();

        for (Map.Entry<String, Integer> entry : history.entrySet()) {

          if (entry.getKey().equalsIgnoreCase(movie.getTitle())) {

            viewsNumber += entry.getValue();
          }
        }
      }
      movie.setViews(viewsNumber);
    }

    // sort movies' list ascendent by number of views;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getViews() > sortedMovies.get(j + 1).getViews()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same number of views, sort them ascendent by name;
          if ((sortedMovies.get(j).getViews() == sortedMovies.get(j + 1).getViews())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies.get(j + 1)
                  .getTitle()) > 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort movies' list descendent by number of views;
      for (int i = 0; i < sortedMovies.size() - 1; i++) {

        for (int j = 0; j < sortedMovies.size() - i - 1; j++) {

          if (sortedMovies.get(j).getViews() < sortedMovies.get(j + 1).getViews()) {

            Collections.swap(sortedMovies, j, j + 1);
          }

          // if two movies have the same number of views, sort them descendent by name;
          if ((sortedMovies.get(j).getViews() == sortedMovies.get(j + 1).getViews())
                  && (sortedMovies.get(j).getTitle().compareTo(sortedMovies.get(j + 1)
                  .getTitle()) < 0)) {

            Collections.swap(sortedMovies, j, j + 1);
          }
        }
      }
    }


    int count = 0;
    for (MovieInputData movie : sortedMovies) {

      if (count < number) {

        // verify if the movie's number of views is 0;
        if (movie.getViews() != 0) {

          boolean flag1 = true;
          boolean flag2 = true;

          // apply year filter;
          if (years.get(0) != null) {

            flag1 = false;
            String yearString = String.valueOf(movie.getYear());
            for (String year : years) {

              if (year.equalsIgnoreCase(yearString)) {

                flag1 = true;
                break;
              }
            }
          }

          // apply genres filter;
          if (genres.get(0) != null) {

            if (!flag1) {

              flag2 = false;
            } else {

              int genresFound = 0;
              for (String genre : genres) {

                for (String movieGenre : movie.getGenres()) {

                  if (genre.equalsIgnoreCase(movieGenre)) {

                    genresFound++;
                  }
                }
              }
              if (genresFound != genres.size()) {

                flag2 = false;
              }
            }
          }

          if (flag1 && flag2) {

            query.add(movie.getTitle());
            count++;
          }
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }
}
