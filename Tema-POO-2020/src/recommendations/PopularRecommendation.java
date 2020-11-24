package recommendations;

import common.Constants;
import entertainment.Genre;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class PopularRecommendation {
  private final List<UserInputData> users;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  private final int id;

  private final String username;

  // the user with the given username;
  private UserInputData givenUser;

  public PopularRecommendation(final List<UserInputData> users, final List<MovieInputData> movies,
                               final List<SerialInputData> serials, final int id,
                               final String username) {
    this.users = users;
    this.movies = movies;
    this.serials = serials;
    this.id = id;
    this.username = username;
    this.givenUser = null;
  }

  /** methods that give the first unseen movie that have the best genre */
  public final JSONObject getPopularRecommendation() {
    JSONObject object = new JSONObject();

    // list that stores all genres;
    List<Genre> genres = new ArrayList<>(Arrays.asList(Genre.values()));

    // map that store every genre and its number of appearences;
    Map<String, Integer> bestGenres = new HashMap<>();

    // find the user in the users list;
    for (UserInputData user : users) {

      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }
    }

    // verify if the user is valid;
    if (givenUser.getSubscriptionType().equalsIgnoreCase("BASIC")) {

      object.put(Constants.ID_STRING, this.id);
      object.put(Constants.MESSAGE, "PopularRecommendation cannot be applied!");
      return object;
    }

    for (Genre genre : genres) {

      boolean flag = false;
      for (UserInputData user : users) {

        Map<String, Integer> history = user.getHistory();

        // search in every user's history to get number of views for every genre;
        for (Map.Entry<String, Integer> entryHistory : history.entrySet()) {

          // if it's a movie, search in movies' list to get its genre;
          for (MovieInputData movie : movies) {

            if (movie.getTitle().equalsIgnoreCase(entryHistory.getKey())) {

              for (String genreMovie : movie.getGenres()) {

                if (genreMovie.equalsIgnoreCase(genre.name())) {

                  // verify if the genre is already in the map;
                  for (Map.Entry<String, Integer> entryGenre : bestGenres.entrySet()) {

                    if (entryGenre.getKey().equalsIgnoreCase(genre.name())) {

                      flag = true;
                      // if it's in the list, grow number of views;
                      entryGenre.setValue(entryGenre.getValue() + entryHistory.getValue());
                      break;
                    }
                  }

                  // if it isn't, then add it
                  if (!flag) {

                    bestGenres.put(genre.name(), entryHistory.getValue());
                  }
                }
              }
            }
          }

          // if it's a serial, search in serials' list to get its genre;
          for (SerialInputData serial : serials) {

            if (serial.getTitle().equalsIgnoreCase(entryHistory.getKey())) {

              for (String genreSerial : serial.getGenres()) {

                if (genreSerial.equalsIgnoreCase(genre.name())) {

                  // verify if the genre is already in the map;
                  for (Map.Entry<String, Integer> entryGenre : bestGenres.entrySet()) {

                    if (entryGenre.getKey().equalsIgnoreCase(genre.name())) {

                      flag = true;
                      // if it's in the list, grow number of views;
                      entryGenre.setValue(entryGenre.getValue() + entryHistory.getValue());
                      break;
                    }
                  }

                  // if it isn't, then add it
                  if (!flag) {

                    bestGenres.put(genre.name(), entryHistory.getValue());
                  }
                }
              }
            }
          }
        }
      }
    }

    // sort the list descendent by number of views;
    Map<String, Integer> bestGenresSorted = bestGenres.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new));

    // get the most viewed genre;
    for (String popularGenre : bestGenresSorted.keySet()) {

     for (MovieInputData movie : movies) {

        boolean flag = false;
        for (String seenMovie : givenUser.getHistory().keySet()) {

          if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

            flag = true;
            break;
          }
        }

        // found one movie that isn't seen;
        if (!flag) {

          for (String genre : movie.getGenres()) {

            if (genre.equalsIgnoreCase(popularGenre)) {

              object.put(Constants.ID_STRING, this.id);
              object.put(Constants.MESSAGE, "PopularRecommendation result: " + movie.getTitle());
              return object;
            }
          }
        }
      }

      // if it hasn't found an unseen movie, then go through serials' list and
      // see if there's any the user hasn't seen;
      for (SerialInputData serial : serials) {

        boolean flag = false;
        for (String seenSerial : givenUser.getHistory().keySet()) {

          if (seenSerial.equalsIgnoreCase(serial.getTitle())) {

            flag = true;
            break;
          }
        }

        // found one serial that isn't seen;
        if (!flag) {

          for (String genre : serial.getGenres()) {

            if (genre.equalsIgnoreCase(popularGenre)) {

              object.put(Constants.ID_STRING, this.id);
              object.put(Constants.MESSAGE, "PopularRecommendation result: " + serial.getTitle());
              return object;
            }
          }
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "PopularRecommendation cannot be applied!");
    return  object;
  }
}
