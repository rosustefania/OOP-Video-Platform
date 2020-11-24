package queries.videos;

import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteMovies {
  private final List<UserInputData> users;

  private final List<MovieInputData> givenMovies;

  private final int id;

  private final String sortType;

  private final int number;

  private final List<String> years;

  private final List<String> genres;

  public FavouriteMovies(final List<UserInputData> users, final List<MovieInputData> givenMovies,
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
}
