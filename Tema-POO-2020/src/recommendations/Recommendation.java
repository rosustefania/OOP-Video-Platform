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
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

public class Recommendation {
  private final List<UserInputData> users;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  private final int id;

  private final String username;

  private final String givenGenre;

  // the user with the given username;
  private UserInputData givenUser;


  public Recommendation(final List<UserInputData> users, final List<MovieInputData> movies,
                                final List<SerialInputData> serials, final int id,
                                final String username, final String givenGenre) {
    this.users = users;
    this.movies = movies;
    this.serials = serials;
    this.id = id;
    this.username = username;
    this.givenGenre = givenGenre;
  }

  /** methods that give the first unseen movie by an user */
  public final JSONObject getStandardRecommendation() {
    JSONObject object = new JSONObject();

    // find the user in the users list;
    for (UserInputData user : users) {

      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }
    }

    // first go through movies' list and see if there's any the user hasn't seen;
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

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "StandardRecommendation result: " + movie.getTitle());
        return object;
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

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "StandardRecommendation result: " + serial.getTitle());
        return object;
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "StandardRecommendation cannot be applied!");
    return object;
  }

  /** methods that give the best unseen movie by an user */
  public final JSONObject getBestUnseenRecommendation() {
    JSONObject object = new JSONObject();

    // list that store all the shows that have a rating
    Map<String, Double> bestShowsUnsorted = new HashMap<>();
    Map<String, Double> bestShowsSorted;

    // find the user in the users list;
    for (UserInputData user : users) {

      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }
    }

    // add all the movies that have rating in the list
    for (MovieInputData movie : movies) {

      movie.mean();
      if (movie.getRatingsMean() != 0) {
        bestShowsUnsorted.put(movie.getTitle(), movie.getRatingsMean());
      }
    }

    // add all the serials that have rating in the list
    for (SerialInputData serial : serials) {

      serial.mean();
      if (serial.getRatingsMean() != 0) {
        bestShowsUnsorted.put(serial.getTitle(), serial.getRatingsMean());
      }
    }

    // sort the list descendent by ratings;
    bestShowsSorted = bestShowsUnsorted
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new));

    // search in the sorted list to see if there's any show that's unseen;
    for (String title : bestShowsSorted.keySet()) {

      boolean flag = false;
      for (String seenTitle : givenUser.getHistory().keySet()) {

        if (seenTitle.equalsIgnoreCase(title)) {
          flag = true;
          break;
        }
      }

      // found one show that isn't seen;
      if (!flag) {

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: " + title);
        return object;
      }
    }

    // if all the best shows are seen, search in the database and find the first one that isn't
    // seen;
    // first go through movies' list and see if there's any the user hasn't seen;
    for (MovieInputData movie : movies) {

      boolean flag = false;
      for (String seenMovie : givenUser.getHistory().keySet()) {

        if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

          //System.out.println(movie.getTitle());
          flag = true;
          break;
        }
      }

      // found one movie that isn't seen;
      if (!flag) {

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: "
                + movie.getTitle());
        return object;
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

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: "
                + serial.getTitle());
        return object;
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation cannot be applied!");
    return object;

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

                  // verify if the genre is already to the map ;
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

                  // verify if the genre is already to the map ;
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

  /** methods that give the first unseen show based on
   * appearences in the favourites lists*/
  public final JSONObject getFavouriteRecommendation() {
    JSONObject object = new JSONObject();

    // map that store favourites shows and their number of appearences;
    Map<String, Integer> favouritesShows = new HashMap<>();

    for (UserInputData user : users) {

      // find the user in the users list;
      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }

      // search in every user's favourites list and calculate the
      // number of appearences for every show;
      for (String favourite : user.getFavoriteMovies()) {

        boolean flag = false;
        // verify if the genre is already to the map ;
        for (Map.Entry<String, Integer> entryFavourite : favouritesShows.entrySet()) {

          if (favourite.equalsIgnoreCase(entryFavourite.getKey())) {

            flag = true;
            // if it's in the list, grow number of views;
            entryFavourite.setValue(entryFavourite.getValue() + 1);
            break;
          }
        }

        //if it isn't, then add it
        if (!flag) {

          favouritesShows.put(favourite, 1);
        }
      }
    }

    // verify if the user is valid;
    if (givenUser.getSubscriptionType().equalsIgnoreCase("BASIC")) {

      object.put(Constants.ID_STRING, this.id);
      object.put(Constants.MESSAGE, "FavoriteRecommendation cannot be applied!");
      return object;
    }

    // sort the list descendent by number of appearences;
    Map<String, Integer> favouritesShowsSorted = favouritesShows
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new));

    // get the most added show in users' favourites lists;
    for (String favouriteShow : favouritesShowsSorted.keySet()) {

      boolean flag = false;
      for (String seenMovie : givenUser.getHistory().keySet()) {

        if (seenMovie.equalsIgnoreCase(favouriteShow)) {

          flag = true;
          break;
        }
      }

      // found one movie that isn't seen;
      if (!flag) {

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "FavoriteRecommendation result: " + favouriteShow);
        return object;
      }

    }

    // if there isn't any that is unseen, search in the database;
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

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "FavoriteRecommendation result: " + movie.getTitle());
        return object;
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

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "FavoriteRecommendation result: "
                + serial.getTitle());
        return object;
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "FavoriteRecommendation cannot be applied!");
    return object;
  }

  /** methods that give all the unseen shows that
   * have a given genre, ascendent by their rating */
  public final JSONObject getSearchRecommendation() {
    JSONObject object = new JSONObject();

    // map that store unseen shows and their rating;
    Map<String, Double> searchShows = new HashMap<>();

    // find the user in the users list;
    for (UserInputData user : users) {

      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }
    }

    // verify if the user is valid;
    if (givenUser.getSubscriptionType().equalsIgnoreCase("BASIC")) {

      object.put(Constants.ID_STRING, this.id);
      object.put(Constants.MESSAGE, "SearchRecommendation cannot be applied!");
      return object;
    }

    // search for unseen movies that have the given genre and add them to the map ;
    for (MovieInputData movie : movies) {

      boolean flag = false;
      for (String seenMovie : givenUser.getHistory().keySet()) {

        if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

          flag = true;
          break;
        }
      }

      // the movie isn't seen, check its genre;
      if (!flag) {

        for (String genre : movie.getGenres()) {

          // if the movie have the given genre add it to the map;
          if (genre.equalsIgnoreCase(givenGenre)) {

            movie.mean(); // calculate movie rating;
            searchShows.put(movie.getTitle(), movie.getRatingsMean());
          }
        }
      }
    }

    // search for unseen serials that have the given genre and add them to the map;
    for (SerialInputData serial : serials) {

      boolean flag = false;
      for (String seenSerial : givenUser.getHistory().keySet()) {

        if (seenSerial.equalsIgnoreCase(serial.getTitle())) {

          flag = true;
          break;
        }
      }

      // the serial isn't seen, check its genre;
      if (!flag) {

        for (String genre : serial.getGenres()) {

          // if the serial have the given genre add it to the map;
          if (genre.equalsIgnoreCase(givenGenre)) {

            serial.mean(); // calculate serial rating;
            searchShows.put(serial.getTitle(), serial.getRatingsMean());
          }
        }
      }
    }

    // if the map is empty, then the recommendation cannot be applied;
    if (searchShows.isEmpty()) {

      object.put(Constants.ID_STRING, this.id);
      object.put(Constants.MESSAGE, "SearchRecommendation cannot be applied!");
      return object;
    }

    // sort the map ascendent by rating;
    Map<String, Double> searchShowsSorted = new TreeMap<>(searchShows);

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "SearchRecommendation result: " + searchShowsSorted.keySet());
    return object;
  }
}
