package queries.videos;

import common.Constants;
import fileio.MovieInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongestMovies {
  private final List<MovieInputData> givenMovies;

  private final int id;

  private final int number;

  private final String sortType;

  private final List<String> years;

  private final List<String> genres;

  public LongestMovies(final List<MovieInputData> givenMovies, final int id, final int number,
                       final String sortType, final List<String> years, final List<String> genres) {
    this.givenMovies = givenMovies;
    this.id = id;
    this.number = number;
    this.sortType = sortType;
    this.years = years;
    this.genres = genres;
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
}
